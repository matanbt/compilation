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

public class IRcommand_Jump_Label extends IRcommand
{
	String label_name;

	/* IRcommand to jump to, used for liveness analysis */
	public IRcommand jump_dst;
	
	public IRcommand_Jump_Label(String label_name, IRcommand jump_dst)
	{
		this.label_name = label_name;
		this.jump_dst = jump_dst;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().jump(label_name);
	}
}
