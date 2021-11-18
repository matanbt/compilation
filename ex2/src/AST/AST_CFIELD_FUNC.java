package AST;

public class AST_CFIELD_FUNC extends AST_CFIELD
{
	/***********************/
	/*  CField ::= funcDec */
	/***********************/
	public AST_DEC_FUNC func;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_CFIELD_FUNC(AST_DEC_FUNC func)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== cField -> funcDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.func = func;
	}

	/*********************************************************/
	/* The printing message for an CFIELD FUNC AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST CFIELD FUNC */
		/********************************************/
		System.out.print("AST NODE CFIELD FUNC\n");

		/***********************************/
		/* RECURSIVELY PRINT FUNC */
		/***********************************/
		if (func != null) func.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"AST_CFIELD_FUNC");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,func.SerialNumber);
	}
}
