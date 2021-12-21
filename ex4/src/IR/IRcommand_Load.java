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
        // TODO: load should be handles differently depends on var.mRole
        MIPSGenerator.getInstance().load(dst, var);
    }
}
