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
import TYPES.TYPE_CLASS;

public class IRcommand_New_Class_Instance_Init extends IRcommand
{
	TEMP dst;  // it's value will be a the address of the new instance
	TYPE_CLASS type_class;

	public IRcommand_New_Class_Instance_Init(TEMP dst, TYPE_CLASS type_class)
	{
		this.dst = dst;
		this.type_class = type_class;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// TODO
	}
}
