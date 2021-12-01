package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_FUNC extends AST_DEC
{
	public AST_TYPE rtnType;
	public String funcName;
	public AST_STMT_LIST body;
	public AST_DEC_FUNC_ARG_LIST argList; // could be null

	// -------------------- Semantic Additions --------------------
	// caches the result of SemantMe, so it could be used on recursive calls from SemantMe
	public TYPE_FUNCTION result_SemantMe;
	// non-null means method of 'belongToClass', and null means it's global function
	public AST_DEC_CLASS belongToClass = null;
	// verify there exist a return (if needed)
	public boolean isReturnExists = false;

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
		this.result_SemantMe = null;
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
		if (argList != null) argList.PrintMe();
		if (body != null)    body.PrintMe();

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
		if (argList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, argList.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
	}


	public TYPE SemantMe()
	{
		TYPE semantic_rtnType = null;
		TYPE_LIST list_argtypes = null; // lists of the semantic types of arguments

		/*******************/
		/* [0] return type */
		/*******************/
		semantic_rtnType = this.rtnType.SemantMe();

		if (semantic_rtnType == null)
		{
			System.out.format(">> ERROR non existing return type %s\n", this.rtnType);
			// TODO deal with error
			System.exit(0);
		}

		this.belongToClass = SYMBOL_TABLE.getInstance().findScopeClass();

		if(belongToClass == null) {
			// we're in global context, so we make sure no same-name declaration
			// TODO - this assumes travis will answer that EXAMPLE-1 = INVALID
			if(SYMBOL_TABLE.getInstance().find(funcName) != null) {
				System.out.format(">> ERROR identifier (%s) is previously declared, " +
						"can't declare global-function\n",funcName);
				// TODO deal with error
				System.exit(0);
			}
		}
		// else: it's a method, will be checked as a CFIELD

		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope(TYPE_FOR_SCOPE_BOUNDARIES.FUNC_SCOPE, this);

		/***************************/
		/* [2] Semant Arguments */
		/***************************/
		for (AST_DEC_FUNC_ARG_LIST it = argList; it  != null; it = it.next)
		{
			// find the TYPE of each
			AST_DEC_FUNC_ARG arg = it.head; // the arg of the iterator
			TYPE semantic_argType = arg.SemantMe();
			if (semantic_argType == null)
			{
				System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,arg.argType);
				// TODO deal with error
				System.exit(0);
			}
			else
			{
				// each argument has a type that we'll want to verify when it'll be called
				list_argtypes = new TYPE_LIST(semantic_argType, list_argtypes);
				// each argument is also a local variable in the function scope
				SYMBOL_TABLE.getInstance().enter(arg.argName, semantic_argType);
			}
		}

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table
		(!!!) needs to be before body.SemantMe because it can have a recursive call */
		/***************************************************/
		result_SemantMe = new TYPE_FUNCTION(semantic_rtnType, funcName, list_argtypes);
		SYMBOL_TABLE.getInstance().enter(funcName, result_SemantMe);

		/*******************/
		/* [3] Semant Body */
		/*******************/
		body.SemantMe();

		// forces non-void-functions to contain at least one RETURN
		if (!isReturnExists && (semantic_rtnType != TYPE_VOID.getInstance())) {
			System.out.format(">> ERROR no return statement exists, when declared-return is (%s) " +
					"for function (%s) ", rtnType.type_name, funcName);
			// TODO deal with error
			System.exit(0);
		}

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();


		return result_SemantMe;
	}

	public boolean isMethod() {
		// NOTE: only relevant AFTER SemantMe
		return belongToClass != null; // TODO SEMANT IT
	}

}