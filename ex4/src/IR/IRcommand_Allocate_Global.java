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

public class IRcommand_Allocate_Global extends IRcommand
{
    String var_name;
    AST_EXP var_value_ast;

    public static int uniqueAllocatedStringNum = 0;

    public IRcommand_Allocate_Global(String var_name, AST_EXP var_value_ast)
    {
        // assumes var_value instanceof AST_EXP_INT or AST_EXP_STRING or AST_EXP_NIL
        this.var_name = var_name;
        this.var_value_ast = var_value_ast;  // can be null (meaning it's a var dec without assignment)
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        String full_var_name = String.format("global_%s", var_name); // Adding convention to global variable

        if (this.var_value_ast == null || this.var_value_ast instanceof AST_EXP_NIL) {
            MIPSGenerator.getInstance().allocateWithIntVal(full_var_name, 0);
        }

        else if (this.var_value_ast instanceof AST_EXP_INT) {
            MIPSGenerator.getInstance().allocateWithIntVal(full_var_name, ((AST_EXP_INT)var_value_ast).value);
        }

        else if (this.var_value_ast instanceof AST_EXP_STRING) {
            // 1. Allocate the string itself
            // The name of the allocated string must be unique, so a unique integer is concatenated
            String allocatedStringLabel = String.format("str_%s_%d", full_var_name, uniqueAllocatedStringNum++);
            String allocatedStringValue = ((AST_EXP_STRING)var_value_ast).value;
            MIPSGenerator.getInstance().allocateString(allocatedStringLabel, allocatedStringValue);

            // 2. Allocate the pointer to the string
            MIPSGenerator.getInstance().allocateByReferenceName(full_var_name, allocatedStringLabel);
        }

        else {
            System.out.println("[DEBUG] Warning: unexpected var_value in IRcommand_Allocate_Global");
        }
    }
}
