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
import TEMP.*;

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

	@Override
	public void updateInSet() {
		super.updateInSet();
		this.in_set.remove(arrPointer);
		this.in_set.add(arrLen);
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		TEMP temp_arrPointer = new SAVED(0);
		// the array contains the length as its first item, so we need to allocate arrLen + 1 words
		MIPSGenerator.getInstance().addi(arrLen, arrLen, 1);
		// initialize all array cells to null (for uninitialized pointer handling)
		MIPSGenerator.getInstance().mallocWordsWithNullInit(temp_arrPointer, arrLen);
		MIPSGenerator.getInstance().addi(arrLen, arrLen, -1);
		// set first item of the array to be its length
		MIPSGenerator.getInstance().storeToHeap(arrLen, temp_arrPointer, 0);
		MIPSGenerator.getInstance().move(arrPointer, temp_arrPointer);
	}
}
