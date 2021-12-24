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

public class IRcommand_Store extends IRcommand {
    IDVariable var;
    TEMP src;

    public IRcommand_Store(IDVariable var, TEMP src) {
        this.src = src;
        this.var = var;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        if (var.mRole == VarRole.GLOBAL) {
            MIPSGenerator.getInstance().storeGlobal(var.mVarName, src);
        }

        else if (var.mRole == VarRole.LOCAL || var.mRole == VarRole.ARG) {
            MIPSGenerator.getInstance().storeToStack(var.getOffset(), src);
        }

        /*
         *  TODO - deal with classes, when implementing their IR
         */
    }
}
