package AST;

// indexable new-expression
public class AST_NEW_EXP_IDX extends AST_NEW_EXP // TODO - extend DEC(lare)
{
	public String idName;
	public AST_EXP expression; // not null

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_NEW_EXP_IDX(String idName, AST_EXP expression)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== newExp -> NEW %s [exp]", idName);

		this.idName = idName;
		this.expression = expression;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.format("AST NODE newExp ID( %s )\n", idName);

		if (expression != null) expression.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"new " + idName);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expression.SerialNumber);
	}
}