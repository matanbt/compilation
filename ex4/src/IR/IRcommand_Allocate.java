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

public class IRcommand_Allocate extends IRcommand
{
	IDVariable var;
	
	public IRcommand_Allocate(IDVariable var)
	{
		this.var = var;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (var.mRole == VarRole.GLOBAL) {
			MIPSGenerator.getInstance().allocate("global_" + var.mVarName);
		}
		else {
			// TODO- do we need more options?
		}
	}
}
