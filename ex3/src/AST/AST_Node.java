package AST;

import TYPES.TYPE;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;

	public abstract void PrintMe();
	public abstract TYPE SemantMe();
}
