package AST;

import TYPES.*;

public abstract class AST_DEC extends AST_Node
{
	public TYPE SemantMe()
	{
		return null;
	}

	/*********************************************************/
	/* The default message for an unknown AST DEC node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST DEC NODE");
	}
}
