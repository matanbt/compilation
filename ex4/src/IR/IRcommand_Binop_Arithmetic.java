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
	/***************/
	/* MIPS me !!! */

	/***************/
	public void MIPSme() {
		MIPSGenerator mips = MIPSGenerator.getInstance();

		TEMP lower_bound = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP upper_bound = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP zero = TEMP_FACTORY.getInstance().getFreshTEMP();

		String lower_bound_lbl = getFreshLabel("lower");
		String upper_bound_lbl = getFreshLabel("upper");
		String end_lbl = getFreshLabel("end");

		/* Do the arithmetic operation */
		if (arithmetic_op.equals("+")) {
			mips.add(this.dst, this.left_t, this.right_t);
		} else if (arithmetic_op.equals("-")) {
			mips.sub(this.dst, this.left_t, this.right_t);
		} else if (arithmetic_op.equals("*")) {
			mips.mul(this.dst, this.left_t, this.right_t);
		} else if (arithmetic_op.equals("/")) {
			/* Runtime check - division by zero */
			mips.li(zero, 0);
			mips.beq(this.right_t, zero, MIPSGenerator.LABEL_STRING_ILLEGAL_DIV_BY_0);

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