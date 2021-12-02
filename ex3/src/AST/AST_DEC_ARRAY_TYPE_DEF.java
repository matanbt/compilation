package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_ARRAY_TYPE_DEF extends AST_DEC
{
	public AST_TYPE type;
	public String id;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_ARRAY_TYPE_DEF(AST_TYPE type, String id)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== arrayTypedef -> ARRAY ID(%s) = type [];\n",id);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.id = id;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST AST_ARRAY_TYPE_DEF NODE\n");

		/**********************************************/
		/* RECURSIVELY PRINT type, then ID NAME ... */
		/**********************************************/
		if (type != null) type.PrintMe();
		System.out.format("ID NAME( %s )\n",id);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("DEF ARRAY TYPE(%s)",id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
	}

	public TYPE SemantMe()
	{
		SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();

		/* 1. Check we are in the global scope */
		if (!table.isGlobalScope())
		{
			/* TODO: Errorize */
			System.out.format(">> ERROR array typedef not in global scope\n");
			System.exit(0);
		}

		/* 2. Check that the identifier name is valid (AKA not used) */
		if (table.find(this.id) != null)
		{
			/* TODO: Errorize */
			System.out.format(">> ERROR identifier already used\n");
			System.exit(0);
		}

		/* 3. Check that the type is valid (AKA defined before) */
		TYPE array_type = this.type.SemantMe();
		if (array_type == null)
		{
			/* TODO: Errorize */
			System.out.format(">> ERROR Invalid type for array\n");
			System.exit(0);
		}

		/* 4. Update symbol table with this new type */
		TYPE_ARRAY array = new TYPE_ARRAY(this.id, array_type);
		table.enter(this.id, array);

		/* 5. Return type of array */
		return array;
	}
}