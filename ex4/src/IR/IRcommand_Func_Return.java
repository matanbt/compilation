/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import TEMP.TEMP;

/*******************/

/* The following command is actually meaningless in IR, but will be used in MIPSme */
public class IRcommand_Func_Return extends IRcommand
{
	/* the temporary holds the return value */
	// Note: could be null in case of void function ("return;")
	public TEMP rtnTemporary;

	public String funcName;
	public String epilogueLabel;

	public IRcommand_Func_Return(TEMP rtnTemporary, String funcName, String epilogueLabel)
	{
		this.rtnTemporary = rtnTemporary;
		this.funcName = funcName;
		this.epilogueLabel = epilogueLabel;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// TODO fill in the future
		if (rtnTemporary != null) {
			// move $v0, $rtnTemporary
		}
		//j epilogueLabel
	}
}
