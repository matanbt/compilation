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

import AST.AST_VAR_SUBSCRIPT;
import MIPS.MIPSGenerator;
import TEMP.TEMP;

/* IR Command : array_set arrPointer, subscriptIndex, src */
public class IRcommand_Array_set extends IRcommand
{
	public TEMP arrPointer;
	public TEMP subscriptIndex;
	public TEMP src;

	public IRcommand_Array_set(TEMP arrPointer, TEMP subscriptIndex, TEMP src)
	{
		this.arrPointer = arrPointer;
		this.subscriptIndex = subscriptIndex;
		this.src = src;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
//		MIPSGenerator.getInstance().?;
	}
}
