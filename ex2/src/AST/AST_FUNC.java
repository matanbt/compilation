package AST;

public class AST_FUNC extends AST_NODE // TODO - extend DEC(lare)
{
	public AST_TYPE rtnType;
	public String funcName;
	public AST_STMT_LIST body;
	public AST_ARG_LIST argList; // could be null

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_FUNC(AST_TYPE rtnType, String funcName, AST_ARG_LIST argList, AST_STMT_LIST body)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== dec -> function declaration %s %s\n", rtnType, funcName);


		this.rtnType = rtnType;
		this.funcName = funcName;
		this.argList = argList;
		this.body = body;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE FUNCTION-DECLARATION\n");


		if (rtnType != null) rtnType.PrintMe();
		if (argList != null) argList.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"FUNC: " + funcName);

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,rtnType.SerialNumber);
		if (argList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,argList.SerialNumber);
	}
}