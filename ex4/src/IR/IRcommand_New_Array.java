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

public class IRcommand_New_Array extends IRcommand
{
	/* arrPointer = new_array arrLen */
	TEMP arrPointer;  // its value will be the address of the new array
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
		// the array contains the length as its first item, so we need to allocate
		MIPSGenerator.getInstance().addi(arrLen, arrLen, 1);
		MIPSGenerator.getInstance().mallocWords(arrPointer, arrLen);
		MIPSGenerator.getInstance().addi(arrLen, arrLen, -1);
		// set first item of the array to be its length
		MIPSGenerator.getInstance().storeToHeap(arrLen, arrPointer, 0);
	}
}
