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
	public int argsCount;
	public int localsCount;

	public IRcommand_Func_Prologue(int argsCount, int localsCount)
	{
		this.argsCount = argsCount; // TODO - we probably won't need it, but i'm keeping it in case we will
		this.localsCount = localsCount;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// MIPSGenerator.getInstance().calleeBackup(); // callee responsibility backup
		// MIPSGenerator.getInstance().prologue(argsCount, localsCount);
	}
}
