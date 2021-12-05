package AST;

import EXCEPTIONS.SemanticException;

import TYPES.TYPE;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;

	/*******************************************/
	/* The line the node appeared when parsing */
	/*******************************************/
	public int lineNumber;

	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}

	public abstract TYPE SemantMe();

	public void throw_error(String message) throws SemanticException {
		throw new SemanticException(String.format("line %d: %s", this.lineNumber, message), this.lineNumber);
	}
}
