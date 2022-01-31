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

import java.util.HashSet;

public class IRcommand_Store extends IRcommand {
    /* var := src */
    IDVariable var;
    TEMP src;

    public IRcommand_Store(IDVariable var, TEMP src) {
        this.src = src;
        this.var = var;
    }

    public void updateInSet()
    {
        this.in_set = new HashSet<TEMP>(this.out_set);
        this.in_set.add(src);
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        if (var.mRole == VarRole.GLOBAL) {
            MIPSGenerator.getInstance().storeByVarName("global_" + var.mVarName, src);
        }

        else if (var.mRole == VarRole.LOCAL || var.mRole == VarRole.ARG) {
            MIPSGenerator.getInstance().storeToStack(var.getOffset(), src);
        }

        else if (var.mRole == VarRole.CFIELD_VAR) {
            System.out.println("[DEBUG] IRcommand_Store shouldn't be called on CFIELD_VAR, use IRcommand_Field_set");
        }

        else {
            System.out.println("[DEBUG] IRcommand_Store with unexpected var.mRole");
        }
    }
}
