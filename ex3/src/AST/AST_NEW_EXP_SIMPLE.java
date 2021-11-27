package AST;

public class AST_NEW_EXP_SIMPLE extends AST_NEW_EXP
{
	public AST_TYPE nType;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_NEW_EXP_SIMPLE(AST_TYPE nType)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== newExp -> NEW type");



		this.nType = nType;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.format("AST NODE SIMPLE-newExp");

		if (nType != null) nType.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"newExp-SIMPLE");

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, nType.SerialNumber);

	}
}