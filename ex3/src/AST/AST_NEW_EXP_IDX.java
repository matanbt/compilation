package AST;

import EXCEPTIONS.SemanticException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

// indexable new-expression
public class AST_NEW_EXP_IDX extends AST_NEW_EXP
{
	public AST_TYPE nType;
	public AST_EXP expression; // not null
	/* Should only be used after a successful call to SemantMe */
	public Integer size;

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

	public TYPE SemantMe() throws SemanticException
	{
		/* 1. Check that type was defined before */
		/* TODO: Make sure this works when merging with everyone's branches */
		TYPE array_type = this.nType.SemantMe();
		if (array_type == null)
		{
			this.throw_error("undefined type for array creation");
		}

		/* 2. Check that type is indeed an array type */
		if (!array_type.isSymbolType())
		{
			this.throw_error("trying to create array from something that isn't an array type");
		}

		/* 3. Check that size of array is integral */
		/* TODO: Does integral mean constant, or just int? - looks like int according to the forum.
		 */
		TYPE index_type = this.expression.SemantMe();
		if (index_type == null || !(index_type instanceof TYPE_INT_INSTANCE))
		{
			this.throw_error("undefined type for size of array");
		}

		if (this.expression instanceof AST_EXP_INT) {
			Integer value = ((AST_EXP_INT) this.expression).value;
			if (value <= 0) {
				this.throw_error("invalid size for array");
			}
			this.size = value;
		}

		/* 4. Return type of array instance */
		TYPE_ARRAY t = new TYPE_ARRAY("", array_type);
		return t.convertSymbolToInstance();
	}
}