package IR;

import TEMP.TEMP;
import TYPES.TYPE_CLASS;

import static MIPS.MIPSGenerator.TEMP_TO_BACKUP_COUNT;

/*
 * Each declared variable in L is represented by the following class
 */
public class IDVariable {

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

    // only for mRole = VarRole.CFIELD_VAR
    TYPE_CLASS type_class = null;

    public IDVariable(String varName, VarRole role, int index) {
        this.mVarName = varName;
        this.mRole = role;
        this.mIndex = index;
    }

    public IDVariable(String varName, VarRole role) {
        // used when the index is irrelevant (like global vars)
        this(varName, role, -1);
    }

    public IDVariable(String varName, VarRole role, TYPE_CLASS type_class) {
        // only for VarRole.CFIELD_VAR & VarRole.CFIELD_FUNC
        this(varName, role);
        this.type_class = type_class;
    }

    /* calculates the offset (in Bytes), whenever it's a local parameter */
    public int getOffset() {
        if (this.mRole == VarRole.ARG) {
            int offset_to_first_arg = 8; // we need to skip "previous-fp" and "return-address"
            return offset_to_first_arg + (4 * this.mIndex);
        }
        else if (this.mRole == VarRole.LOCAL) {
            int offset_to_first_local = -4;
            return - (4 * TEMP_TO_BACKUP_COUNT)
                    + offset_to_first_local
                    - (4 * this.mIndex);
        }
        else if(this.mRole == VarRole.CFIELD_VAR) {
            return type_class.getFieldOffset(this.mVarName) * 4;
        }

        // Shouldn't get here, no offsets for other variable roles
        return 0;
    }

    /* --------------------------- THIS ---------------------------- */
    /* a IDVariable dedicated to `this` inside a method
    * Basically it represents the first argument of the function */
    public static IDVariable thisInstance = null;

    public static IDVariable getThisInstance() {
        if (thisInstance != null) {
            return thisInstance;
        }
        /* We observe that in method that first argument is always the "this" of the method,
        * meaning the class-instance that invoked the method */
        thisInstance = new IDVariable("this", VarRole.ARG, 0);
        return thisInstance;
    }
    /* --------------------------- THIS ---------------------------- */

}
