package AST;

// indexable new-expression
public class AST_NEW_EXP_IDX extends AST_NEW_EXP
{
	public AST_TYPE nType;
	public AST_EXP expression; // not null

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_NEW_EXP_IDX(AST_TYPE nType, AST_EXP expression, int lineNumber)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();


		System.out.format("====================== newExp -> NEW type [exp]");

		this.nType = nType;
		this.expression = expression;
		this.lineNumber = lineNumber;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.format("AST NODE newExp Indexed\n");

		if (nType != null) nType.PrintMe();
		if (expression != null) expression.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"newExp-IDX");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, nType.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, expression.SerialNumber);
	}
}