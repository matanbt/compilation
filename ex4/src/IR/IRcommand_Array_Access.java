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
		 * & returns offset_in_words (the offset from arrPointer of the relevant cell) */

		MIPSGenerator mips = MIPSGenerator.getInstance();

		/* Runtime check - out of bound array access */
		// Case (1): subscriptIndex < 0
		mips.bltz(subscriptIndex, MIPSGenerator.LABEL_STRING_ACCESS_VIOLATION);

		// Case (2): subscriptIndex >= arrLen
		TEMP arrLen = TEMP_FACTORY.getInstance().getFreshTEMP();
		mips.loadFromHeap(arrLen, arrPointer, 0);
		mips.bge(subscriptIndex, arrLen, MIPSGenerator.LABEL_STRING_ACCESS_VIOLATION);

		TEMP offset_in_words = TEMP_FACTORY.getInstance().getFreshTEMP();
		// the first item in the array is it's length, so the index val should be incremented by 1
		mips.addi(offset_in_words, subscriptIndex, 1);

		return offset_in_words;
	}
}
