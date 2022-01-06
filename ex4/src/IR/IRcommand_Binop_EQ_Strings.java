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
import TEMP.SAVED;


public class IRcommand_Binop_EQ_Strings extends IRcommand {
	/* dst = str_eq str_1, str_2
	 * Where str_1 and str_2 are pointers to the strings on the heap,
	 * and dst will be 1 if the strings are equal, 0 otherwise     */

	public TEMP str_1;
	public TEMP str_2;
	public TEMP dst;
	private int CHAR_SIZE = 1;

	public IRcommand_Binop_EQ_Strings(TEMP dst, TEMP str_1, TEMP str_2) {
		this.dst = dst;
		this.str_1 = str_1;
		this.str_2 = str_2;
	}
	/***************/
	/* MIPS me !!! */

	/***************/
	public void MIPSme() {
		MIPSGenerator mips = MIPSGenerator.getInstance();

		// labels:
		String lbl_not_equal = getFreshLabel("STR_EQ_not_equal");
		String lbl_end = getFreshLabel("STR_EQ_end");
		String lbl_loop = getFreshLabel("STR_EQ_loop");

		TEMP address_of_str_1_i = new SAVED(1);
		TEMP address_of_str_2_i = new SAVED(2);
		TEMP val_of_curr_str_1_i = new SAVED(3);
		TEMP val_of_curr_str_2_i = new SAVED(4);

		// dst = 1
		mips.li(dst, 1);

		mips.move(address_of_str_1_i, str_1);
		mips.move(address_of_str_2_i, str_2);

		// loop
		mips.label(lbl_loop);

		mips.loadByteFromHeap(val_of_curr_str_1_i, address_of_str_1_i, 0);
		mips.loadByteFromHeap(val_of_curr_str_2_i, address_of_str_2_i, 0);

		// check equality: if str1[i] != str2[i]
		mips.bne(val_of_curr_str_1_i, val_of_curr_str_2_i, lbl_not_equal);

		// check end of strings: if str1[i] == 0 --> strings are equal
		mips.beqz(val_of_curr_str_1_i, lbl_end);

		// i++
		mips.addi(address_of_str_1_i, address_of_str_1_i, CHAR_SIZE);
		mips.addi(address_of_str_2_i, address_of_str_2_i, CHAR_SIZE);

		// return to loop start
		mips.jump(lbl_loop);

		// not equal
		mips.label(lbl_not_equal);
		// dst = 0
		mips.li(dst, 0);

		// end
		mips.label(lbl_end);
	}
}