package AST;

public class AST_STMT_RETURN extends AST_STMT
{
	public AST_EXP exp; // could be null (e.g. void functions)

	public AST_STMT_RETURN(AST_EXP exp, int lineNumber)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.print("====================== stmt -> RETURN ");
		if (exp != null) System.out.print(" exp ");
		System.out.print("SEMICOLON\n");

		this.exp = exp;
		this.lineNumber = lineNumber;
	}

	// overloading contractors:
	public AST_STMT_RETURN(int lineNumber)
	{
		this(null, lineNumber);
	}


	public void PrintMe()
	{
		System.out.print("AST NODE RETURN STMT\n");
		if (exp != null) exp.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"RETURN");

		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
	}
}
