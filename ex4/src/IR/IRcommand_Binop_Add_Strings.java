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
import java.util.HashSet;
import java.util.List;

public class IRcommand_Binop_Add_Strings extends IRcommand {
	/* dst = string_add left_str, right_str
	 * Where left_str and right_str are pointers to the strings on the heap,
	 * and dst will eventually point to the new allocated string      */
	public TEMP left_str;
	public TEMP right_str;
	public TEMP dst;
	private MIPSGenerator mips = MIPSGenerator.getInstance();
	private int char_size = mips.getCharSizeInBytes();

	public IRcommand_Binop_Add_Strings(TEMP dst, TEMP left_str, TEMP right_str) {
		this.dst = dst;
		this.left_str = left_str;
		this.right_str = right_str;
	}

	public void updateInSet()
	{
		this.in_set = new HashSet<TEMP>(this.out_set);
		this.in_set.remove(dst);
		this.in_set.add(left_str);
		this.in_set.add(right_str);
	}

	/***************/
	/* MIPS me !!! */

	/***************/
	public void MIPSme() {
		TEMP res_string_len = new SAVED(0);  // the len in bytes of the new string
		TEMP address_of_curr_str_i = new SAVED(1);
		TEMP val_of_curr_str_i = new SAVED(2);
		TEMP address_of_new_str_i = new SAVED(3);
		TEMP did_loop_twice = new SAVED(4);
		// each loop happens twice- first with left_str, and then with right_str. after the first loop, did_loop_twice = 0
		// after the second loop, did_loop_twice = 1

		String curr_loop, end_curr_loop;

		// new string len = 1 (in chars unit)
		// starting from 1 because last cell = 0
		mips.li(res_string_len, char_size);

		/* (1) Calculate the length of the new string */

		curr_loop = getFreshLabel("STR_ADD_loop_calc_len_lbl");
		end_curr_loop = getFreshLabel("STR_ADD_end_loop_calc_len_lbl");

		// address_of_curr_str_i = left_str (for adding to res_string_len the length of left_str)
		mips.move(address_of_curr_str_i, left_str);

		mips.li(did_loop_twice, -1);

		// loop
		mips.label(curr_loop);
		// val_of_curr_str_i = curr_str[i]
		mips.loadByteFromHeap(val_of_curr_str_i, address_of_curr_str_i, 0);
		// if curr_str[i] == 0, exit loop
		mips.beqz(val_of_curr_str_i, end_curr_loop);
		// res_string_len ++
		mips.addi(res_string_len, res_string_len, char_size);
		// i++
		mips.addi(address_of_curr_str_i, address_of_curr_str_i, char_size);

		mips.jump(curr_loop);

		// loop end
		mips.label(end_curr_loop);
		mips.addi(did_loop_twice, did_loop_twice, 1);

		// address_of_curr_str_i = right_str (for adding to res_string_len the length of right_str)
		mips.move(address_of_curr_str_i, right_str);
		mips.beqz(did_loop_twice, curr_loop);  // if we did loop only once, do it again

		/* (2) allocate memory for the new String */
		mips.malloc(dst, res_string_len);

		/* (3) Copy the Strings content to the new string */
		mips.move(address_of_new_str_i, dst);

		curr_loop = getFreshLabel("STR_ADD_loop_copy_content_lbl");
		end_curr_loop = getFreshLabel("STR_ADD_end_loop_copy_content_lbl");

		// address_of_curr_str_i = left_str (for copying left_str content to the new string)
		mips.move(address_of_curr_str_i, left_str);

		mips.li(did_loop_twice, -1);

		// loop
		mips.label(curr_loop);
		// val_of_curr_str_i = curr_str[i]
		mips.loadByteFromHeap(val_of_curr_str_i, address_of_curr_str_i, 0);
		// if val_of_curr_str_i == 0, exit loop
		mips.beqz(val_of_curr_str_i, end_curr_loop);
		// dst[i] = curr_str[i]
		mips.storeByteToHeap(val_of_curr_str_i, address_of_new_str_i, 0);
		// i++
		mips.addi(address_of_curr_str_i, address_of_curr_str_i, char_size);
		mips.addi(address_of_new_str_i, address_of_new_str_i, char_size);

		mips.jump(curr_loop);

		// loop end
		mips.label(end_curr_loop);
		mips.addi(did_loop_twice, did_loop_twice, 1);

		// address_of_curr_str_i = right_str (for adding to res_string_len the length of right_str)
		mips.move(address_of_curr_str_i, right_str);
		mips.beqz(did_loop_twice, curr_loop);  // if we did loop only once, do it again

		/* Add 0 at the end of the new String */
		mips.li(val_of_curr_str_i, 0);
		mips.storeByteToHeap(val_of_curr_str_i, address_of_new_str_i, 0);
	}
}