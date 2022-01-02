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
 * Allocates the string `val`, so it'll **later** be loaded into TEMP `t`.
 */
public class IRcommand_Allocate_String extends IRcommand
{
	String val;
	TEMP t;

	public IRcommand_Allocate_String(String val, TEMP t)
	{
		this.val = val;
		this.t = t;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		String var_name = String.format("str_%s_%d", this.val, this.t.getSerialNumber());
		MIPSGenerator.getInstance().allocateString(var_name, this.val);
	}
}
