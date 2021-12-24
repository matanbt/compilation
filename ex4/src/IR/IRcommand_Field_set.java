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

/* IR Command : field_set objectPointer, fieldName, src */
public class IRcommand_Field_set extends IRcommand
{
	public TEMP objectPointer;
	public String fieldName; // TODO when implementing classes: might need to be modified to int representing the field index
	public TEMP src;

	public IRcommand_Field_set(TEMP objectPointer, String fieldName, TEMP src)
	{
		this.objectPointer = objectPointer;
		this.fieldName = fieldName;
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
