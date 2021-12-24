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


public class IRcommand_Binop_EQ_Strings extends IRcommand {
	/* dst = str_eq left_t, right_t
	 * Where left_t and right_t are pointers to the strings on the heap,
	 * and dst will be 1 if the strings are equal, 0 otherwise     */

	public TEMP left_t;
	public TEMP right_t;
	public TEMP dst;

	public IRcommand_Binop_EQ_Strings(TEMP dst, TEMP left_t, TEMP right_t) {
		this.dst = dst;
		this.left_t = left_t;
		this.right_t = right_t;
	}
	/***************/
	/* MIPS me !!! */

	/***************/
	public void MIPSme() {
		// TODO
	}
}