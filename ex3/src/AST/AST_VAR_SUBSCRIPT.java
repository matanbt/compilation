package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_ARRAY_INSTANCE;
import TYPES.TYPE_INT;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
		this.lineNumber = lineNumber;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}

	public TYPE SemantMe()
	{
		/* 1. Check that var was defined before */
		/* TODO: Make sure this works when merging with everyone's branches */
		TYPE var_type = this.var.SemantMe();
		if (var_type == null)
		{
			/* TODO: Errorize */
			System.out.println(">> ERROR unknown variable");
			System.exit(0);
		}

		/* 2. Make sure var is indeed TYPE_ARRAY_INSTANCE */
		if (!(var_type instanceof TYPE_ARRAY_INSTANCE))
		{
			/* TODO: Errorize */
			System.out.println(">> ERROR trying to index something that isn't an array");
			System.exit(0);
		}

		/* 3. Check that index of array is integral */
		/* TODO: Does integral mean constant, or just int? */
		TYPE index_type = this.subscript.SemantMe();
		if (index_type == null)
		{
			/* TODO: Errorize */
			System.out.println(">> ERROR undefined type for index of array");
			System.exit(0);
		}

		if (!(index_type instanceof TYPE_INT))
		{
			/* TODO: Errorize */
			System.out.println(">> ERROR Trying to access array not using an integer");
			System.exit(0);
		}

		/* 4. Return type of array instance */
		return ((TYPE_ARRAY_INSTANCE) var_type).getElementType();
	}
}
