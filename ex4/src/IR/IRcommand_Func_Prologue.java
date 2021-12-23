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

/* The following command is actually meaningless in IR, but will be used in MIPSme */
public class IRcommand_Func_Prologue extends IRcommand
{
	public int localsCount;

	public IRcommand_Func_Prologue(int localsCount)
	{
		this.localsCount = localsCount;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// MIPSGenerator.getInstance().calleeBackup(); // callee responsibility backup
		// MIPSGenerator.getInstance().prologue(localsCount);
	}
}
