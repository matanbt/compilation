/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

import TEMP.*;
import MIPS.*;

public class IRcommand_Load extends IRcommand {
    TEMP dst;
    IDVariable var;

    public IRcommand_Load(TEMP dst, IDVariable var) {
        this.dst = dst;
        this.var = var;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        if (var.mRole == VarRole.GLOBAL) {
            MIPSGenerator.getInstance().loadGlobal(dst, var.mVarName);
        }

        else if (var.mRole == VarRole.LOCAL || var.mRole == VarRole.ARG) {
            MIPSGenerator.getInstance().loadFromStack(dst, var.getOffset());
        }

        else if (var.mRole == VarRole.CFIELD_VAR) {
            System.out.println("[DEBUG] IRcommand_Load shouldn't be called on CFIELD_VAR, use IRcommand_Field_get");
        }

        else {
            System.out.println("[DEBUG] IRcommand_Load with unexpected var.mRole");
        }
    }
}
