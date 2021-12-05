package AST;

import TYPES.TYPE;

public class AST_CFIELD extends AST_Node
{
	/********************************/
	/*  CField ::= funcDec | varDec */
	/********************************/
	public AST_DEC dec;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_CFIELD(AST_DEC dec, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== cField -> Dec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		// dec MUST BE instance of "limited" DEC_VAR or DEC_FUNC. This is enforced by the grammar (cup file).
		this.dec = dec;
		this.lineNumber = lineNumber;
	}

	/*********************************************************/
	/* The printing message for an CFIELD FUNC AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST CFIELD FUNC */
		/********************************************/
		System.out.print("AST NODE CFIELD\n");

		/***********************************/
		/* RECURSIVELY PRINT FUNC */
		/***********************************/
		if (dec != null) dec.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,dec.SerialNumber);
	}

	public TYPE SemantMe() {
		// TODO: (1) Check that each CField has different name. (2) Make sure AST_DEC_VAR.isCField is taken care of.
		return null;
	}
}
