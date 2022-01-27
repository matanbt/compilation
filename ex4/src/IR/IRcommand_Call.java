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

/* Invocation of *global* functions
*     call f, arg1, arg2, ...
*     rtnTemp = call f, arg1, arg2, ...  */
public class IRcommand_Call extends IRcommand {
    String funcName;
    List<TEMP> argsTempList;
    TEMP rtnTemp; // temporary to save return value, null means no save


    public IRcommand_Call(String funcName, List<TEMP> argsTempList, TEMP rtnTemp) {
        this.funcName = funcName; //  funcName is required to be WITHOUT our MIPS conventions
        this.argsTempList = argsTempList;
        this.rtnTemp = rtnTemp;
    }

    @Override
    public void updateInSet() {
        super.updateInSet();
        if (rtnTemp != null)
        {
            this.in_set.remove(rtnTemp);
        }

        this.in_set.addAll(argsTempList);
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        MIPSGenerator mips = MIPSGenerator.getInstance();

        // add our global function convention here
        String funcLabel = String.format("func_%s", funcName);

        mips.functionCallerPrologue(argsTempList);
        mips.functionJumpAndLink(funcLabel);
        mips.functionCallerEpilogue(argsTempList.size(), rtnTemp);
    }
}
