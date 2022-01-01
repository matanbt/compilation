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
//    private TEMP instancePtr = null;  // TODO DELETE
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

    // TODO DELETE
//    public IDVariable(String varName, VarRole role, TEMP instancePtr, TYPE_CLASS type_class) {
//        // only for VarRole.CFIELD_VAR & VarRole.CFIELD_FUNC
//        this(varName, role);
//        this.instancePtr = instancePtr;
//        this.type_class = type_class;
//    }


    /* calculates the offset, whenever it's a local parameter */
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
            return type_class.getFieldOffset(this.mVarName);
        }

        // Shouldn't get here, no offsets for other variable roles
        return 0;
    }

    // TODO DELETE
//    // only for mRole = VarRole.CFIELD_VAR
//    public TEMP getInstancePtr(){
//        if (this.mRole != VarRole.CFIELD_VAR){
//            System.out.println("[DEBUG] IDVariable.getInstancePtr() was called on non-CFIELD_VAR");
//        }
//        return instancePtr;
//    }
}
