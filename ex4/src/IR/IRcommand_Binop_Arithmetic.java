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
import TEMP.*;
import MIPS.*;

import java.util.HashSet;


public class IRcommand_Binop_Arithmetic extends IRcommand {
	// Arithmetic operation between integers

	private int INT_UPPER_BOUND = 32767;
	private int INT_LOWER_BOUND = -32768;

	public TEMP left_t;
	public TEMP right_t;
	public TEMP dst;
	public String arithmetic_op;  // can be: "+", "-", "*", "/"

	public IRcommand_Binop_Arithmetic(TEMP dst, TEMP left_t, TEMP right_t, String arithmetic_op) {
		this.dst = dst;
		this.left_t = left_t;
		this.right_t = right_t;
		this.arithmetic_op = arithmetic_op;
	}

	public void updateInSet()
	{
		this.in_set = new HashSet<TEMP>(this.out_set);
		this.in_set.remove(dst);
		this.in_set.add(left_t);
		this.in_set.add(right_t);
	}

	/***************/
	/* MIPS me !!! */

	/***************/
	public void MIPSme() {
		MIPSGenerator mips = MIPSGenerator.getInstance();

		TEMP lower_bound = new SAVED(0);
		TEMP upper_bound = new SAVED(1);

		String lower_bound_lbl = getFreshLabel("BINOP_ARITHMETIC_reached_lower_bound");
		String upper_bound_lbl = getFreshLabel("BINOP_ARITHMETIC_reached_upper_bound");
		String end_lbl = getFreshLabel("BINOP_ARITHMETIC_end");

		/* Do the arithmetic operation */
		if (arithmetic_op.equals("+")) {
			mips.add(this.dst, this.left_t, this.right_t);
		}

		else if (arithmetic_op.equals("-")) {
			mips.sub(this.dst, this.left_t, this.right_t);
		}

		else if (arithmetic_op.equals("*")) {
			mips.mul(this.dst, this.left_t, this.right_t);
		}

		else if (arithmetic_op.equals("/")) {
			/* Runtime check - division by zero */
			mips.beqz(this.right_t, MIPSGenerator.LABEL_STRING_ILLEGAL_DIV_BY_0);

			mips.div(this.dst, this.left_t, this.right_t);
		}


		/* Compare against case (1) */
		mips.li(lower_bound, INT_LOWER_BOUND);
		mips.blt(this.dst, lower_bound, lower_bound_lbl);

		/* Compare against case (2) */
		mips.li(upper_bound, INT_UPPER_BOUND);
		mips.blt(upper_bound, this.dst, upper_bound_lbl);

		/* Case (3): just end it */
		mips.jump(end_lbl);

		/* Handle case (1) */
		mips.label(lower_bound_lbl);
		mips.li(this.dst, INT_LOWER_BOUND);
		mips.jump(end_lbl);

		/* Handle case (2) */
		mips.label(upper_bound_lbl);
		mips.li(this.dst, INT_UPPER_BOUND);

		/* End */
		mips.label(end_lbl);
	}
}