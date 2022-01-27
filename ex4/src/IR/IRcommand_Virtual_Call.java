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

import java.util.HashSet;
import java.util.List;

/* Invocation of global functions
 * virtual_call classInstance methodName arg1, arg2, ...
 * rtnTemp = virtual_call classInstance methodName arg1, arg2, ...  */
public class IRcommand_Virtual_Call extends IRcommand {
    TEMP classObject;         // the address of the object invoking the method
    int methodOffset;         // offset of the method in vTable
    List<TEMP> argsTempList;  // includes `this` as first argument
    TEMP rtnTemp;             // temporary to save return value, null means no save

    /* funcName is required to be WITHOUT conventions */
    public IRcommand_Virtual_Call(TEMP classObject, int methodOffset, List<TEMP> argsTempList,
                                  TEMP rtnTemp) {
        this.classObject = classObject;
        this.methodOffset = methodOffset * MIPSGenerator.getInstance().WORD_SIZE;
        this.argsTempList = argsTempList;
        this.rtnTemp = rtnTemp;
    }

    public void updateInSet()
    {
        this.in_set = new HashSet<TEMP>(this.out_set);
        this.in_set.remove(rtnTemp);
        this.in_set.add(classObject);
        this.in_set.addAll(argsTempList);
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        MIPSGenerator mips = MIPSGenerator.getInstance();

        mips.functionCallerPrologue(argsTempList);
        mips.methodJumpAndLink(classObject, methodOffset);
        mips.functionCallerEpilogue(argsTempList.size(), rtnTemp);
    }
}
