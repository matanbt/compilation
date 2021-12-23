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


public class IRcommand_Binop_Add_Strings extends IRcommand {
	/* dst = string_add left_t, right_t
	 * Where left_t and right_t are pointers to the strings in the heap,
	 * and dst will eventually point to the new allocated string      */

	public TEMP left_t;
	public TEMP right_t;
	public TEMP dst;

	public IRcommand_Binop_Add_Strings(TEMP dst, TEMP left_t, TEMP right_t) {
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