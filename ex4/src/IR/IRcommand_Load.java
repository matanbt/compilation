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

        /*
         *  TODO - deal with classes, when implementing their IR
         */

    }
}
