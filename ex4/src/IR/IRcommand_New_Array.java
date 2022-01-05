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

public class IRcommand_New_Array extends IRcommand
{
	/* arrPointer = new_array arrLen */
	TEMP arrPointer;  // it's value will be a the address of the new array
	TEMP arrLen;  // number of items in the array

	public IRcommand_New_Array(TEMP arrPointer, TEMP arrLen)
	{
		this.arrPointer = arrPointer;
		this.arrLen = arrLen;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// don't forget : add +1 to len (for storing the len of the arr inside len)
	}
}
