package AST;

import EXCEPTIONS.SemanticException;
import TYPES.TYPE;

public class AST_NEW_EXP_SIMPLE extends AST_NEW_EXP
{
	public AST_TYPE nType;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_NEW_EXP_SIMPLE(AST_TYPE nType, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== newExp -> NEW type\n");



		this.nType = nType;
		this.lineNumber = lineNumber;
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

	public TYPE SemantMe() throws SemanticException
	{
		/* 1. Check that type was defined before */
		TYPE class_type = this.nType.SemantMe();
		if (class_type == null)
		{
			this.throw_error("undefined type for class creation");
		}

		/* 2. Check that type is indeed a class type */
		if (!class_type.isClassSymbol())
		{
			this.throw_error("trying to create class instance from something that isn't a class type");
		}

		/* 3. Return type of class instance */
		return class_type.convertSymbolToInstance();
	}
}