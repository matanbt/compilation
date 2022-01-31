/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TEMP.*;

/*******************/

public abstract class IRcommand_Array_Access extends IRcommand
{
	protected TEMP arrayAccess(TEMP arrPointer, TEMP subscriptIndex){
		/* performs runtime check
		 * & returns offset_in_bytes (the offset from arrPointer of the relevant cell) */

		MIPSGenerator mips = MIPSGenerator.getInstance();

		mips.beqz(arrPointer, MIPSGenerator.LABEL_STRING_INVALID_PTR_DREF);

		/* Runtime check - out of bound array access */
		// Case (1): subscriptIndex < 0
		mips.bltz(subscriptIndex, MIPSGenerator.LABEL_STRING_ACCESS_VIOLATION);

		// Case (2): subscriptIndex >= arrLen
		TEMP arrLen = new SAVED(0);
		mips.loadFromHeap(arrLen, arrPointer, 0);
		mips.bge(subscriptIndex, arrLen, MIPSGenerator.LABEL_STRING_ACCESS_VIOLATION);

		TEMP offset_in_bytes = new SAVED(1);
		// the first item in the array is its length, so the index val should be incremented by 1
		mips.addi(offset_in_bytes, subscriptIndex, 1);

		// offset in words --> offset in bytes
		TEMP word_size = new SAVED(0);
		mips.li(word_size, mips.WORD_SIZE);
		mips.mul(offset_in_bytes, offset_in_bytes, word_size);

		return offset_in_bytes;
	}
}
