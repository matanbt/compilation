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

/* IR Command : dst = field_access objPointer var */
public class IRcommand_Field_access extends IRcommand
{
	private TEMP dst;
	private TEMP objectPointer;
	private IDVariable field;  // field var

	public IRcommand_Field_access(TEMP dst, TEMP objectPointer, IDVariable field)
	{
		this.dst = dst;
		this.objectPointer = objectPointer;
		this.field = field;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().loadFromHeap(dst, objectPointer, field.getOffset());
	}
}