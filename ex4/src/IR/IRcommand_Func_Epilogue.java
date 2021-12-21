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

/* The following command is actually meaningless in IR, but will be used in MIPSme */
public class IRcommand_Func_Epilogue extends IRcommand
{

	public IRcommand_Func_Epilogue()
	{
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// MIPSGenerator.getInstance().calleeRestore(); // callee responsibility backup
		// MIPSGenerator.getInstance().epilogue();
	}
}
