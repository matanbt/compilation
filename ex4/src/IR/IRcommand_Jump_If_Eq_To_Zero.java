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

public class IRcommand_Jump_If_Eq_To_Zero extends IRcommand
{
	TEMP t;
	String label_name;

	/* IRcommand to jump to, used for liveness analysis */
	public IRcommand jump_dst;


	public IRcommand_Jump_If_Eq_To_Zero(TEMP t, String label_name, IRcommand jump_dst)
	{
		this.t          = t;
		this.label_name = label_name;
		this.jump_dst = jump_dst;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().beqz(t,label_name);
	}
}
