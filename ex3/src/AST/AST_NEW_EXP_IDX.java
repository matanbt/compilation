package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_ARRAY_INSTANCE;
import TYPES.TYPE_INT;

// indexable new-expression
public class AST_NEW_EXP_IDX extends AST_NEW_EXP
{
	public AST_TYPE nType;
	public AST_EXP expression; // not null

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_NEW_EXP_IDX(AST_TYPE nType, AST_EXP expression)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();


		System.out.format("====================== newExp -> NEW type [exp]");

		this.nType = nType;
		this.expression = expression;
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

	public TYPE SemantMe()
	{
		SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();

		/* 1. Check that type was defined before */
		/* TODO: Make sure this works when merging with everyone's branches */
		TYPE array_type = this.nType.SemantMe();
		if (array_type == null)
		{
			/* TODO: Errorize */
			System.out.println(">> ERROR undefined type for array creation");
			System.exit(0);
		}

		/* 2. Check that type is indeed an array type */
		if (!(array_type instanceof TYPE_ARRAY))
		{
			/* TODO: Errorize */
			System.out.println(">> ERROR trying to create array from something that isn't an array");
			System.exit(0);
		}

		/* 3. Check that size of array is integral */
		/* TODO: Does integral mean constant, or just int?
		   Int will have some implications on the next assignment.
		 */
		TYPE index_type = this.expression.SemantMe();
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

		/* Return type of array instance */
		return new TYPE_ARRAY_INSTANCE((TYPE_ARRAY) array_type);
	}
}