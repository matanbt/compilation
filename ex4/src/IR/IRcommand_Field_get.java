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
public class IRcommand_Field_get extends IRcommand
{
	private TEMP dst;
	private TEMP objectPointer;
	private IDVariable field;  // field var

	public IRcommand_Field_get(TEMP dst, TEMP objectPointer, IDVariable field)
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
		// invalid pointer dereference case - assumes that pointers to null\not initialized class instances are = 0  // TODO - verify that! (need to do this in arrays, fields, locals, globals)
		MIPSGenerator.getInstance().beqz(objectPointer, MIPSGenerator.LABEL_STRING_INVALID_PTR_DREF);

		MIPSGenerator.getInstance().loadFromHeap(dst, objectPointer, field.getOffset());
	}
}
