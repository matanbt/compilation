# Part 0: Changes to IRcommand
We need to add to each IRcommand several fields.
* For all of the IRcommands: Add 2 `HashSet<TEMP>`: One for IN temporaries, and the other
for OUT temporaries. Also, we need to add a command that will update IN based on OUT
(slide 14 in the slides).
* For `IRcommand_Jump_Label` and `IRcommand_Jump_If_Eq_To_Zero`, we need to add
an IRcommand field, called `jump_dst`, that will hold the IRcommand that we jump to
(in case of equality to zero in the second case).

We need to create a new module, that will hold a mapping between temporary
registers and actual registers. The module should hold a static `HashMap` between
integers (`TEMP`'s serial numbers) and integers (`$t0-$t9`).

# Part 1: Extracting Commands for CFG Creation
We need to traverse the IR list, and extract each function's commands. Note that
we are calling functions from part 2-5, so be aware.  
Part 6 will deal with the small changes to the MIPS code.

**Traverse IR(`ir_list`):**
* Initialize `in_function` to `false`.
* For each node `ir` in `ir_list`:
    * If `ir` is of type of function prologue:
        * Create a new list `commands` of IRcommands.
        * Set `in_function = true`.
    * If `ir` is not of type of function epilogue AND `in_function == true`:
        * Append `ir` to `commands`.
    * Else:
        * Set `in_function = false`
        * Call *Create CFG* on `commands`, and get `graph`.
        * Call *Liveness Analysis* on `graph`
        * Call *Create Interference Graph* on `commands`, and get `graph`.
        * Call *Color Graph* on `graph`.


# Part 2: Creating the CFG
Create new classes: `Vertex` and `Graph`.

**`Vertex`**
* IRcommand `ir` - the IRcommand assosciated with this vertex.
* List of Vertex `edges` - all of the outgoing edges from this vertex.

**`Graph`**
* A list of vertices. We should add elements to this list in a LIFO manner
(AKA last vertex inserted is in position 0), that is a demand of the liveness analysis.

**Create CFG(`commands`):**
* Create new empty `Graph`, called `g`.

* Create a `HashMap` from IRcommands to vertices, called `map`.
* For each `ir` in `commands`:
    * Create `Vertex` from the given `ir`, called `v`.
    * Add `v` to `g`.
    * Add a mapping from `ir` to `v` in `map`.

* For each `ir` in `commands`:
    * If `ir` is of type `IRcommand_Jump_Label`:
        * Set `v_in = map[ir]`
        * Set `v_out = map[jump_dst]` (see part 0).
        * Add edge `v_in -> v_out`
    * If `ir` is of type `IRcommand_Jump_If_Eq_To_Zero`:
        * Set `v_in = map[ir]`
        * Set `v_out = map[jump_dst]` (see part 0).
        * Add edge `v_in -> v_out`
        * Set `v_out = map[ir.next]`
        * Add edge `v_in -> v_out`
    * Else:
        * Set `v_in = map[ir]`
        * Set `v_out = map[ir.next]`
        * Add edge `v_in -> v_out`

* Return the newly created graph `g`

# Part 3: Liveness Analysis
**Liveness Iteration(`graph`):**
* Set `changed` to `false`.
* For `v` in `graph.vertices`:
    * Back up `v.ir.in` and `v.ir.out`.
    * Create empty HashSet `out`.
    * For each `v_out` in `v.edges`:
        * Add `v_out.in` to `out`.
    * Set `v.ir.out` to `out`
    * Update `v.ir.in` based on `v.ir.out` (see part 0).
    * Check if there was any change, if so, set `changed` to `true`.
* Return `changed`

**Liveness Analysis(`graph`):**
* Set `changed` to `true`.
* while `changed`:
    * Set `changed` to *Liveness Iteration*'s run on `graph`.

# Part 4: Constructing the Interference Graph
This time we are creating an undirected graph, which supports disabling vertices and edges.

**`Vertex`**
* `TEMP t` - holds the temporary assosciated with this vertex.
* `bool is_on` - whether or not this vertex is on or not (disabled).
* `List<Edge> edges` - a list of all of the edges that are connected to this vertex.
* A function `edgesCount()`, that counts how many edges are on.

**`Edge`**
* `Vertex u`.
* `Vertex v`.
* A function `isOn()`, which just checks that both `u,v` are on.

**`Graph`** 
* A list of vertices.

**Create Interference Graph(`commands`):**
* Create a mapping from `TEMP`s to vertices, called `map`.
* Create a new list of vertices.

* For `ir` in `commands`:
    * For each temp `t` in `ir.out`:
        * If `t` isn't in `map` (new vertex):
            * Create new `Vertex v` from `t`.
            * Add it to the list of vertices.
            * Add it to `map`.

* For `ir` in `commands`:
    * For each temp `t` in `ir.out`:
        * Add edges from `map[t]` to every other vertex in `ir.out`.

* Create new graph from the list of vertices, and return it.

# Part 5: K-Coloring
In this part, it is important to note that `K=10`, since we have registers `$t0-$t9`.
Also, we rely on the existence of the static mapping from `TEMP`s to real registers,
as described in part 0. I will refer to it as `Palette` (since we are dealing with colors, get it?),
but please choose a more appropriate name.

**Create Stack(`graph`):**
* Create new empty `Stack<Vertex>`.
* while `true`:
    * Set `stuck` to `true`.
    * For each `v` in `graph`:
        * if `v.is_on` and `v.edgesCount() < K`:
            * Set `stuck` to `false`.
            * Set `v.is_on` to `false`
            * Add `v` to the stack.
    * if `stuck`:
        * Crash.
* Return the stack.

**Color Graph(`graph`):**
* Initialize stack with *Create Stack* on `graph`.
* While stack isn't empty:
    * Pop element from stack, `v`.
    * Set `v.is_on` to `true`.
    * Set `found_color` to `false`.
    * For `i` in `0..K-1`:
        * For `e` in `v.edges`:
            * If `e.isOn()` and `Palette[e.u] == i`:
                * Continue.
        * Set `Palette[v]` to `i`
        * Set `found_color` to `true`.
    * If `!found_color`:
        * Crash.


# Part 6: MIPS Changes
They are really small: We need to tell each MIPS command to use the real
registers instead of the `TEMP`s we passed to it. Luckily, we won't have to
change any signature, but can instead use `Palette` to translate from `TEMP` to
the real register. So we just need to look in the mapping, and to change the
actual MIPS from something like `Temp_%d` to `$t%d`.