# L-Compiler Project
<img src="https://user-images.githubusercontent.com/63508603/169641115-43e457d6-6dd5-48ae-ab7e-d8018e1e6fe9.png" width=600>

Building a compiler from scratch, as final project for Compilation course.
- The compiler compile and run the made up language *L* (which is somwhere between C and Java).
- The implementation was done mainly on Java, and the generated code is for MIPS architecture.

## How does the compiler works?
- Our compiler performs: 
  - **Lexical Analysis:** Using `jlex` (Added in ex1).
  - **Syntax Analysis:** Using `cup` (added in ex2).
  - **Semantic Analysis** (added in ex3).
  - ***MIPS*-code generation** (added in ex4).   
  - **Execution:** The generated *MIPS* code is then run using `SPIM` (*MIPS* simulator).

## Environment Setup
- **Requirements:** *Making* the compiler should be done in *Linux* and requires the following dependencies:
  - Ubuntu
  - Java 8
  - Install [`JFlex`](https://jflex.de/)
  - Install [`Cup`](http://www2.cs.tum.edu/projects/cup/)
  - Install `SPIM` - `$ sudo apt-get install spim xspim`.

## Usage - Compile & Run
In the following steps we'll demonstrate compiling and running the *L* source file `code.l` and write its output to `code_output.txt`.
1. **Compile the compiler:** *Make* the compiler (from `ex4` dir):
   - ``$ make compile``
   - The compiler is now saved as binary named `COMPILER`.
   - Once the compiler is created it can be *reused*, and this step may not be repeated.
2. **Compile *with* the compiler:**  We may use `COMPILER` to compile *L* code files.
   - `$ java -jar COMPILER code.l compiler_run.log`
   - This run creates two artifaces - `compiler_run.log` which logs the latest compilation, and `MIPS.txt` which contains the generated code. 
3. **Run the L program:** Now we use MIPS to run the generated MIPS code.
   - `$ mips -f MIPS.txt #> code_output.txt`
4. **Enjoy** your code output in `code_output.txt`. You've just brought *L* to life!

## Files
- The directories `ex{1,2,3}` are intermediate version of `ex4`, which contain the final project.

