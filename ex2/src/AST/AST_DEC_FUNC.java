package AST;

public class AST_DEC_FUNC extends AST_DEC
{
	public AST_TYPE rtnType;
	public String funcName;
	public AST_STMT_LIST body;
	public AST_DEC_FUNC_ARG_LIST argList; // could be null

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_FUNC(AST_TYPE rtnType, String funcName, AST_DEC_FUNC_ARG_LIST argList, AST_STMT_LIST body)
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

	public AST_DEC_FUNC(AST_TYPE rtnType, String funcName, AST_STMT_LIST body)
	{
		this(rtnType, funcName, null, body);
	}


	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.format("AST NODE FUNCTION-DECLARATION (%s) \n", funcName);


		if (rtnType != null) rtnType.PrintMe();
		if (body != null)    body.PrintMe();
		if (argList != null) argList.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"DEC-FUNC(" + funcName + ")");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, rtnType.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
		if (argList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, argList.SerialNumber);
	}
}