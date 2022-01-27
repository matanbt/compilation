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

/* IR Command : dst = array_get arrPointer, subscriptIndex */
public class IRcommand_Array_Get extends IRcommand_Array_Access
{
	public TEMP dst;
	public TEMP arrPointer;
	public TEMP subscriptIndex;

	public IRcommand_Array_Get(TEMP dst, TEMP arrPointer, TEMP subscriptIndex)
	{
		this.dst = dst;
		this.arrPointer = arrPointer;
		this.subscriptIndex = subscriptIndex;
	}

	@Override
	public void updateInSet() {
		super.updateInSet();
		this.in_set.remove(dst);
		this.in_set.add(arrPointer);
		this.in_set.add(subscriptIndex);
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().loadFromHeap(dst, arrPointer, this.arrayAccess(arrPointer, subscriptIndex));
	}
}
