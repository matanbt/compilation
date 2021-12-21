package IR;

/*
 * Each declared variable in L is represented by the following class
 */
public class IDVariable {

    /* index is used for indexing variables such as function arguments, locals etc */
    /* its counting could be useful, for example when counting locals in a function */
    public static int indexCounter = 0;

    /* the name this variable had in L */
    /* For example: `void foo(int x, int y)`, 'y' will get mVarName="y"  */
    public String mVarName;

    /* the role this variable plays in L code */
    /* For example: `void foo(int x, int y)`, 'y' will get mRole=ARG  */
    public VarRole mRole;

    /* the index (starting 0) of the variable, that is its ordinal as part of arguments, locals, etc */
    /* For example: `void foo(int x, int y)`, 'y' will get mIndex=1  */
    /* Note: mIndex is used for simplification, rather than calculating the offset in ASTs */
    private int mIndex;

    public IDVariable(String varName, VarRole role) {
        this.mVarName = varName;
        this.mRole = role;
        this.mIndex = indexCounter;
        indexCounter++;
    }

    /* calculates the offset, whenever it's a local parameter */
    public int getOffset() {
        if (this.mRole == VarRole.ARG) {
            return 8 + (4 * this.mIndex);
        }
        else if (this.mRole == VarRole.LOCAL) {
            return -4 - (4 * this.mIndex);
        }

        // Shouldn't get here, no offsets for other variable roles
        return 0;
    }

    /*
     * Resets the global index counter
     * useful when indexing parameters or local variables
     */
    public static void resetIndexCounter() {
        indexCounter = 0;
    }

    public static int getIndexCounter() {
        return indexCounter;
    }
}
