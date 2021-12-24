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

public class IRcommand_Allocate_String extends IRcommand
{
	String val;
	int idx;

	public IRcommand_Allocate_String(String val, int idx)
	{
		this.val = val;
		this.idx = idx;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().allocateString(val, idx);
	}
}
