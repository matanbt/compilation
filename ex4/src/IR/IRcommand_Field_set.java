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
	private IDVariable field;  // field var
	private TEMP src;

	public IRcommand_Field_set(TEMP objectPointer, IDVariable field, TEMP src)
	{
		this.objectPointer = objectPointer;
		this.field = field;
		this.src = src;
	}

	@Override
	public void updateInSet() {
		super.updateInSet();
		this.in_set.add(src);
		this.in_set.add(objectPointer);
	}


	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// invalid pointer dereference case - assumes that pointers to null\not initialized class instances are = 0
		MIPSGenerator.getInstance().beqz(objectPointer, MIPSGenerator.LABEL_STRING_INVALID_PTR_DREF);

		MIPSGenerator.getInstance().storeToHeap(src, objectPointer, field.getOffset());
	}
}
