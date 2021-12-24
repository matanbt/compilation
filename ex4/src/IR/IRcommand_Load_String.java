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

public class IRcommand_Load_String extends IRcommand
{
	TEMP dst;
	String val;

	public IRcommand_Load_String(TEMP dst, String val)
	{
		this.dst      = dst;
		this.val = val;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().loadString(dst, val);
	}
}
