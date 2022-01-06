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
import TEMP.SAVED;
import MIPS.MIPSGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IRcommand_Binop_Add_Strings extends IRcommand {
	/* dst = string_add left_str, right_str
	 * Where left_str and right_str are pointers to the strings on the heap,
	 * and dst will eventually point to the new allocated string      */

	public TEMP left_str;
	public TEMP right_str;
	public TEMP dst;
	private int CHAR_SIZE = 1;

	public IRcommand_Binop_Add_Strings(TEMP dst, TEMP left_str, TEMP right_str) {
		this.dst = dst;
		this.left_str = left_str;
		this.right_str = right_str;
	}
	/***************/
	/* MIPS me !!! */

	/***************/
	public void MIPSme() {
		MIPSGenerator mips = MIPSGenerator.getInstance();
		TEMP curr_string;
		TEMP res_string_len = new SAVED(0);
		TEMP address_of_curr_str_i = new SAVED(1);
		TEMP val_of_curr_str_i = new SAVED(2);
		TEMP address_of_new_str_i = new SAVED(3);

		String curr_loop, end_curr_loop;

		List<TEMP> strings = new ArrayList<TEMP>(Arrays.asList(left_str, right_str));

		// new string len = 1 (last cell = 0)
		mips.li(res_string_len, CHAR_SIZE);

		/* Calculate the length of the new string */
		for (int j = 0; j < strings.size(); j++) {
			curr_loop = getFreshLabel("STR_ADD_loop_calc_len_lbl");;
			end_curr_loop = getFreshLabel("STR_ADD_end_loop_calc_len_lbl");

			curr_string = strings.get(j);

			// address_of_curr_str_i = curr_string
			mips.move(address_of_curr_str_i, curr_string);

			// loop
			mips.label(curr_loop);
			// val_of_curr_str_i = curr_str[i]
			mips.loadByteFromHeap(val_of_curr_str_i, address_of_curr_str_i, 0);
			// if curr_str[i] == 0, exit loop
			mips.beqz(val_of_curr_str_i, end_curr_loop);
			// res_string_len ++
			mips.addi(res_string_len, res_string_len, CHAR_SIZE);
			// i++
			mips.addi(address_of_curr_str_i, address_of_curr_str_i, CHAR_SIZE);

			mips.jump(curr_loop);

			// loop end
			mips.label(end_curr_loop);
		}

		/* allocate memory for the new String */
		mips.malloc(dst, res_string_len);

		/* Copy the Strings content to the new string */
		mips.move(address_of_new_str_i, dst);

		for (int j = 0; j < strings.size(); j++) {
			curr_loop = getFreshLabel("STR_ADD_loop_copy_content_lbl");
			end_curr_loop = getFreshLabel("STR_ADD_end_loop_copy_content_lbl");

			curr_string = strings.get(j);

			// address_of_curr_str_i = curr_string
			mips.move(address_of_curr_str_i, curr_string);

			// loop
			mips.label(curr_loop);
			// val_of_curr_str_i = curr_str[i]
			mips.loadByteFromHeap(val_of_curr_str_i, address_of_curr_str_i, 0);
			// if val_of_curr_str_i == 0, exit loop
			mips.beqz(val_of_curr_str_i, end_curr_loop);
			// dst[i] = curr_str[i]
			mips.storeByteToHeap(val_of_curr_str_i, address_of_new_str_i, 0);
			// i++
			mips.addi(address_of_curr_str_i, address_of_curr_str_i, CHAR_SIZE);
			mips.addi(address_of_curr_str_i, address_of_new_str_i, CHAR_SIZE);

			mips.jump(curr_loop);

			// loop end
			mips.label(end_curr_loop);
		}

		/* Add 0 at the end of the new String */
		mips.li(val_of_curr_str_i, 0);
		mips.storeByteToHeap(val_of_curr_str_i, address_of_new_str_i, 0);
	}
}