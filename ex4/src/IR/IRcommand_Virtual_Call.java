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

import TEMP.TEMP;

import java.util.List;

/* Invocation of global functions
 * virtual_call classInstance methodName arg1, arg2, ...
 * rtnTemp = virtual_call classInstance methodName arg1, arg2, ...  */
public class IRcommand_Virtual_Call extends IRcommand {
    TEMP classObject; // the address of the object invoking the method
    String methodName; // TODO replace by offset
    String className; // could be the overriding class, meaning it's dynamic binding
    List<TEMP> argsTempList;
    TEMP rtnTemp; // temporary to save return value, null means no save

    /* funcName is required to be WITHOUT conventions */
    public IRcommand_Virtual_Call(TEMP classObject, String className, String funcName, List<TEMP> argsTempList,
                                  TEMP rtnTemp) {
        this.classObject = classObject;
        this.className = className;
        this.methodName = funcName;
        this.argsTempList = argsTempList;
        this.rtnTemp = rtnTemp;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        String methodLabel = String.format("method_%s_%s", className, methodName); // we inject our conventions here
        // caller prologue (push arguments to stack in reverse):
        // push argument object class (just insert it as argsList[0])
        // for (TEMP arg: argsTempList[::-1]) {}
        // jalr calculated_method_address
        // save $ra to temp (if not null)
        // caller epilogue...

    }
}
