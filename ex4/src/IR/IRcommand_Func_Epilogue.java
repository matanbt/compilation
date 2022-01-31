/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import MIPS.MIPSGenerator;

/*******************/

/* The following command is actually meaningless in IR, but will be used in MIPSme */
public class IRcommand_Func_Epilogue extends IRcommand
{
	private int localsCount;

	public IRcommand_Func_Epilogue(int localsCount)
	{
		this.localsCount = localsCount;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().functionEpilogue(localsCount);
	}
}
