# Compilation Exercise 4 Design Document

## Intro
This document will explain the different API the new exercise exposes to us, how to use it, and the changes we need to make.

## Previous Assumptions
In the previous exercises, we assumed the following:
* A call to `SemantMe` from an AST node will return either `null`, or an instance type, represeting the value of the expression.
* All of the definition of classes, functions, and array typedefs are in the global scope.
* Everything is of size 4 bytes (AKA a `word` in MIPS): Integers, string pointers, class pointers, etc.

# New API

## Temporary Registers
In the first and second part of the exercise, we can use temporary registers in order to perform our calculations. Their API is fairly simple:

* Creation: We don't create them explicitly, but call a temporary registers factory. So, to get a new temporary, simply run:
```java
TEMP_FACTORY factory = TEMP_FACTORY.getInstance();
TEMP t = factory.getFreshTEMP();
```
* We can get the serial number of the `TEMP` by calling `t.getSerialNumber()`
* When we want to translate a statement to an IR, we call `IRme()` on that AST node, and the return type of that node will be the `TEMP` holding the result. Of course, if it is a statement without a return value, we will return `null`.

## Intermediate Representation
We represent IR in the code using the `IRcommand` class (found in IR/IRcommand.java).  
It is fairly simple, with the only field being `label_counter`, which allows us to get label numbers for the different labels the commands might need.  
In addition, the class has a method named `MIPSme`, which converts the IR to MIPS.  
For example, let's look at the binary operation of addition: In the exercise document, it is stated that `a + b` in L has 3 cases:
1. If `a + b <= -32768`, we return `-32768`.
2. If `a + b >= 32767`, we return `32767`.
3. Else, we return `a + b`.

One might choose to implement it in the following code, inside `MIPSme` of IR/IRcommand_Binop_Add_Integers.java:
```java
public void MIPSme()
{
    MIPSGenerator mips = MIPSGenerator.getInstance();

    TEMP lower_bound;
    TEMP upper_bound;

    String lower_bound_lbl = getFreshLabel("lower");
    String upper_bound_lbl = getFreshLabel("upper");
    String end_lbl = getFreshLabel("end");

    /* Add the numbers */
    mips.add(this.dst, this.t1, this.t2);
    
    /* Compare against case (1) */
    mips.li(lower_bound, -32768);
    mips.blt(this.dst, lower_bound, lower_bound_lbl);
    
    /* Compare against case (2) */
    mips.li(upper_bound, 32767)
    mips.bge(this.dst, upper_bound, upper_bound_lbl);
    
    /* Case (3): just end it */
    mips.jump(end_lbl);
    
    /* Handle case (1) */
    mips.label(lower_bound_lbl);
    mips.li(this.dst, -32768);
    mips.jump(end_lbl);

    /* Handle case (2) */
    mips.label(upper_bound_lbl);
    mips.li(this.dst, 32767);

    /* End */
    mips.label(end_lbl);
}
```

## `IRme`
For every node, we now have a new function, called `IRme`, that would allow us to parse the node into an IR.

Let's first look at a simple example:  
Let's say we want to load a constant value, so according to recitation 9 slides (slide #4), we simply write `t1=7`. Now let's do that in code:
```java
/* File: AST_EXP_INT.java */
/* Getting a new temporary to hold the result */
TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();

/* Get the IR command to load a constant integer */
IRcommandConstInt cmd = new IRcommandConstInt(t, this.value);

/* Add the command to the global list of commands */
IR.getInstance().Add_IRcommand(cmd);

/* Return the temporary register holding the result */
return t;
```

One thing that is worth noting about the last example is that there is a global list of all of the IR commands.
We need to add every command we create to it, even if we don't return a value from it.

## `MIPSme`
This is a method inside every `IRcommand` class, and in it we simply write the conversion from IR to MIPS.  
See the above implementation of `MIPSme` as an example.

# Exercise Overview
As was said in the intro of the exercise PDF, there are 3 main parts to the exercise:
1. Recursively traverse the AST to create an IR of the program.
2. Translate IR to MIPS, using temporary registers.
3. Perform liveness analysis, build the interference graph, and reallocate the
temporaries into physical registers.

We can currently do some of parts 1 and 2, and in week 12 we will learn about part 3.  
We are not required to be efficient, we can create "stupid" IR/MIPS code.

# Metadata
In exercise 3, we were told we can annotate the AST with metadata.  
To the best of my understanding, these are some of the most useful (and sometimes mandatory) ones.

## Globals
In every MIPS assembly code, we can find 2 segments: `.data` and `.text`.  
In my opinion, we should annotate global variable declarations with some flag, so
we can know whether it is a global or not.  
If it is a global, we can call `MIPSGenerator.allocate(String var_name);` and allocate it
in the `.data` segment.
In order to read it, we can call `MIPSGenerator.load(TEMP dst, String var_name);`,
and in order to write to it, we can call `MIPSGenerator.store(String var_name, TEMP src);`.

Currently, there is a part that still baffles me in the MIPS code generator, and
that is that the `allocate` function inserts the `.data` segment header before allocating
the requested global, so what happens if I want to enter a function after that? But we should talk about it in person.

## Functions
Right now, our functions don't save a lot of metadata. Some info I thought would be useful:
* Number of arguments
* Number of variables defined in the function.
This metadata would be useful when we will create the stack frame.

## Classes and Arrays
Have not been taught yet when talking about the IR to MIPS part, so I'm waiting
with talking about them.

## Symbol Table
Currently, the symbol table is destroyed once we are finished with the semantic analysis.  
In my opinion, we should create a somewhat duplicate symbol table, albeit simpler, that
will hold the data about the temporary registers the program uses right now. For example:
```
void foo()
{
    int x := 3;
    if (3 + 2 > 5)
    {
        int x := 5;
        PrintInt(x);
    }
}
```
Right now, we have no way of knowing which `x` is the `PrintInt` line refering to.  
However, if we create a mini symbol table (without all of the semantic checks, just dump into it),
and if we save the temporary into it, we would be able to do it.  

My suggestion still raises a question:  
For a function to efficiently manage its stack frame, it must know the number of
variables that would be allocated in its run. However, it can get quite messy when we handle
things like in the example above: Do we really need to go over all of the statements in the
function body, and only then do the actual IR/MIPS generation? Seems problematic.  

A possible solution is just to create a mini stack frame when we enter each scope, and let
each scope handle the amount of variable it needs. That would mean adding that metadata to
functions, ifs, and whiles.

# Starting to Work
In my opinion, there are several small missions we can start on from now:
* Working on the mini symbol table.
* Working on simple IR: variable declarations, binops, ifs, whiles, etc.
* Working on the **bare minimum** implementation of a function: We just need
to create the main function to run simple tests (A point in which our previous exercise lacked,
was the inability to run tests early on).
* Change the `tester.py` and `test_generator.py` files to support the new output (Notice that we need to add the SPIM intro message to the test generator).
