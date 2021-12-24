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

import TEMP.TEMP;

/* IR Command : dst = array_access arrPointer, subscriptIndex */
public class IRcommand_Array_access extends IRcommand
{
	public TEMP dst;
	public TEMP arrPointer;
	public TEMP subscriptIndex;

	public IRcommand_Array_access(TEMP dst, TEMP arrPointer, TEMP subscriptIndex)
	{
		this.dst = dst;
		this.arrPointer = arrPointer;
		this.subscriptIndex = subscriptIndex;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
//		MIPSGenerator.getInstance().?;
	}
}
