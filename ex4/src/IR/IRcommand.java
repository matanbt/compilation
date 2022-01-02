/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import TEMP.TEMP;

import java.util.HashSet;

/*******************/

public abstract class IRcommand
{
	/*****************/
	/* Label Factory */
	/*****************/
	protected static int label_counter=0;
	public    static String getFreshLabel(String msg)
	{
		return String.format("Label_%d_%s",label_counter++,msg);
	}

	/* Fields used for liveness analysis */
	public HashSet<TEMP> in_set = new HashSet<TEMP>();
	public HashSet<TEMP> out_set = new HashSet<TEMP>();

	/**
	 * Update the IN set, based on the OUT set (see slides for
	 * definition of IN/OUT sets).
	 * The calculation should be as follows: If the given IR command is
	 * `y = f(x_1, x_2, ..., x_n)`
	 * then `in_set = {x_1, x_2, ..., x_n} + out_set - {y}`, where '+' means union,
	 * and '-' means set subtraction.
	 */
	public void updateInSet()
	{
		/* Most IRs would need to override this, based on the above comment. */
		this.in_set = new HashSet<TEMP>(this.out_set);
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public abstract void MIPSme();
}
