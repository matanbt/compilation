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

	/*
	 * Load String: Take a string defined already in `.data`, and load its address
	 * to a temporary variable. This does not allocate the string!
	 */
	public IRcommand_Load_String(TEMP dst, String val)
	{
		this.dst = dst;
		this.val = val;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		String str_name = String.format("str_%s_%d", this.val, this.dst.getSerialNumber());
		MIPSGenerator.getInstance().loadString(dst, str_name);
	}
}
