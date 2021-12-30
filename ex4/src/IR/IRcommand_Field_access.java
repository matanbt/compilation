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

/* IR Command : dst = field_access objPointer fieldName */
public class IRcommand_Field_access extends IRcommand
{
	public TEMP dst;
	public TEMP objectPointer;
	public String fieldName; // TODO when implementing classes: might need to be modified to int representing the field index

	public IRcommand_Field_access(TEMP dst, TEMP objectPointer, String fieldName)
	{
		this.dst = dst;
		this.objectPointer = objectPointer;
		this.fieldName = fieldName;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
//		MIPSGenerator.getInstance().?;
	}
}
