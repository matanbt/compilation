package AST;

public class AST_NEW_EXP extends AST_Node // TODO - extend DEC(lare)
{
	public String idName;
	public AST_EXP expression;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_NEW_EXP(String idName, AST_EXP expression)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== newExp -> function declaration %s %s\n", rtnType, funcName);


		this.idName = idName;
		this.expression = expression;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE newExp\n");


		if (idName != null) idName.PrintMe();
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
		if (expression != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expression.SerialNumber);
	}
}