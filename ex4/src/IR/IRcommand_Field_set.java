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

/* IR Command : field_set objectPointer, var, src */
public class IRcommand_Field_set extends IRcommand
{
	private TEMP objectPointer;
	private IDVariable var;  // field var
	private TEMP src;

	public IRcommand_Field_set(TEMP objectPointer, IDVariable var, TEMP src)
	{
		this.objectPointer = objectPointer;
		this.var = var;
		this.src = src;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().storeToHeap(src, objectPointer, var.getOffset());
	}
}
