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
import TEMP.TEMP;

public class IRcommand_Global_Var_Dec extends IRcommand
{
	String var_name;
	AST_EXP var_value;

	public IRcommand_Global_Var_Dec(String var_name, AST_EXP var_value)
	{
		// assumes var_value instanceof AST_EXP_INT or AST_EXP_STRING or AST_EXP_NIL
		this.var_name = var_name;
		this.var_value = var_value;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (this.var_value instanceof AST_EXP_INT) {
			MIPSGenerator.getInstance().global_var_init_int(var_name, ((AST_EXP_INT)var_value).value);
		}

		else if (this.var_value instanceof AST_EXP_STRING) {
			MIPSGenerator.getInstance().global_var_init_string(var_name, ((AST_EXP_STRING)var_value).value);
		}

		else if (this.var_value instanceof AST_EXP_NIL) {
			MIPSGenerator.getInstance().global_var_init_nil(var_name);
		}

		else {
			System.out.println("[DEBUG] Warning: unexpected var_value in IRcommand_Global_Var_Dec");
		}
	}
}

