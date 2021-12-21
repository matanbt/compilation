package AST;

import EXCEPTIONS.SemanticException;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_ARRAY_TYPE_DEF extends AST_DEC
{
	public AST_TYPE type;
	public String id;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_ARRAY_TYPE_DEF(AST_TYPE type, String id, int lineNumber)
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
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
		this.id = id;
		this.lineNumber = lineNumber;
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

	public TYPE SemantMe() throws SemanticException
	{
		SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();

		/* 1. Check that the identifier name is valid (AKA not used) */
		if (table.find(this.id) != null)
		{
			this.throw_error("identifier already used");
		}

		/* 2. Check that the type is valid (AKA defined before) */
		TYPE array_type = this.type.SemantMe();
		if (array_type == null || array_type.canBeAssigned())
		{
			this.throw_error("Invalid type for array");
		}

		/* 3. Update symbol table with this new type */
		TYPE_ARRAY array = new TYPE_ARRAY(this.id, array_type);
		table.enter(this.id, array);

		return null;
	}
}