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

import AST.AST_EXP;
import AST.AST_EXP_INT;
import AST.AST_EXP_NIL;
import AST.AST_EXP_STRING;
import MIPS.MIPSGenerator;

public class IRcommand_Global_Var_Dec extends IRcommand
{
    String var_name;
    AST_EXP var_value;

    public IRcommand_Global_Var_Dec(String var_name, AST_EXP var_value)
    {
        // assumes var_value instanceof AST_EXP_INT or AST_EXP_STRING or AST_EXP_NIL
        this.var_name = var_name;
        this.var_value = var_value;  // can be null (meaning it's a var dec without assignment)
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        if (this.var_value == null || this.var_value instanceof AST_EXP_NIL) {
            MIPSGenerator.getInstance().allocateWithIntVal(var_name, 0);
        }

        else if (this.var_value instanceof AST_EXP_INT) {
            MIPSGenerator.getInstance().allocateWithIntVal(var_name, ((AST_EXP_INT)var_value).value);
        }

        else if (this.var_value instanceof AST_EXP_STRING) {
            MIPSGenerator.getInstance().allocateString("str_global_" + var_name, ((AST_EXP_STRING)var_value).value);
            MIPSGenerator.getInstance().allocateByReferenceName("global_" + var_name,"str_global_" + var_name);
        }

        else {
            System.out.println("[DEBUG] Warning: unexpected var_value in IRcommand_Global_Var_Dec");
        }
    }
}
