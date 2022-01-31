/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import MIPS.MIPSGenerator;
import TEMP.TEMP;

/*******************/

/* The following command is actually meaningless in IR, but will be used in MIPSme */
public class IRcommand_Func_Return extends IRcommand
{
	/* the temporary holds the return value */
	// Note: could be null in case of void function ("return;")
	public TEMP rtnTemporary;

	public String funcName;
	public String epilogueLabel;

	public IRcommand_Func_Return(TEMP rtnTemporary, String funcName, String epilogueLabel)
	{
		this.rtnTemporary = rtnTemporary;
		this.funcName = funcName;
		this.epilogueLabel = epilogueLabel;
	}

	@Override
	public void updateInSet() {
		super.updateInSet();
		if (rtnTemporary != null)
		{
			this.in_set.add(rtnTemporary);
		}
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (rtnTemporary != null) {
			MIPSGenerator.getInstance().setReturn(rtnTemporary);
		}
		MIPSGenerator.getInstance().jump(epilogueLabel);
	}
}
