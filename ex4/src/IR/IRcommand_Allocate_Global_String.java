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
import TEMP.TEMP;

/*
 * Allocates the string `val` in memory location labeled as `strGlobalName`
 */
public class IRcommand_Allocate_Global_String extends IRcommand
{
	String val;
	String strGlobalName;

	public IRcommand_Allocate_Global_String(String val, String strGlobalName)
	{
		// assumes strGlobalName is unique
		this.val = val;
		this.strGlobalName = strGlobalName;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().allocateString(this.strGlobalName, this.val);
	}
}
