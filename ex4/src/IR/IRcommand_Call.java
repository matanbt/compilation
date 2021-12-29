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

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.List;

/* Invocation of global functions
*     call f, arg1, arg2, ...
*     rtnTemp = call f, arg1, arg2, ...  */
public class IRcommand_Call extends IRcommand {
    String funcName;
    List<TEMP> argsTempList;
    TEMP rtnTemp; // temporary to save return value, null means no save

    /* funcName is required to be WITHOUT our MIPS conventions */
    public IRcommand_Call(String funcName, List<TEMP> argsTempList, TEMP rtnTemp) {
        this.funcName = funcName;
        this.argsTempList = argsTempList;
        this.rtnTemp = rtnTemp;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        String funcLabel = String.format("func_%s", funcName); // we inject our conventions here
        // caller prologue (push arguments to stack in reverse)
        // for (TEMP arg: argsTempList[::-1]) {}
        // jal funcName
        // save $ra to temp (if not null)
        // caller epilogue

    }
}
