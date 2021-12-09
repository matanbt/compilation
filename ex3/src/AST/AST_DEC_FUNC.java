package AST;

import EXCEPTIONS.SemanticException;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_FUNC extends AST_DEC
{
	public AST_TYPE rtnType;
	public String funcName;
	public AST_STMT_LIST body;
	public AST_DEC_FUNC_ARG_LIST argList; // could be null

	// -------------------- Semantic Additions --------------------
	// non-null means method of 'encompassingClass', and null means it's global function
	public TYPE_CLASS encompassingClass = null;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_FUNC(AST_TYPE rtnType, String funcName, AST_DEC_FUNC_ARG_LIST argList, AST_STMT_LIST body, int lineNumber)
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
		this.lineNumber = lineNumber;
	}

	public AST_DEC_FUNC(AST_TYPE rtnType, String funcName, AST_STMT_LIST body, int lineNumber)
	{
		this(rtnType, funcName, null, body, lineNumber);
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

	// SemantMe Part 1: Analyze the signature of the function
	private TYPE_FUNCTION SemantMe_FuncSignature() throws SemanticException {
		/*******************/
		/* Semant return type */
		/* Note that semantic analysis for rtnType is special bc it can be void */
		/*******************/
		TYPE semantic_rtnType_signature = rtnType.SemantMe(); // the type of the signature (null means 'void')

		TYPE semantic_rtnType = null; // the actual instance-type we expect to get in return statement, although we allow void
		if(semantic_rtnType_signature != null) {
			semantic_rtnType = semantic_rtnType_signature.convertSymbolToInstance();
		}

		/*******************/
		/* Check that func name is valid */
		/*******************/
		this.encompassingClass = SYMBOL_TABLE.getInstance().findScopeClass();

		if(encompassingClass == null) {
			// we're in global context, so we make sure no same-name declaration
			if(SYMBOL_TABLE.getInstance().find(funcName) != null) {
				this.throw_error(String.format( "identifier (%s) is previously declared, " +
						"can't declare global-function\n", funcName));
			}
		}
		// else: it's a method, will be checked as a CFIELD


		/***************************/
		/* [2] Semant Arguments */
		/***************************/
		TYPE_LIST list_argTypes = null; // lists of the semantic types of arguments
		for (AST_DEC_FUNC_ARG_LIST it = argList; it  != null; it = it.next)
		{
			// find the TYPE of each
			AST_DEC_FUNC_ARG arg = it.head; // the arg of the iterator
			TYPE semantic_argType = arg.SemantMe();
			if (semantic_argType == null)
			{
				this.throw_error(String.format("non existing type %s\n",arg.argType));
				// TODO deal with error
				System.exit(0);
			}
			// each argument has a type that we'll want to verify when it'll be called
			semantic_argType = semantic_argType.convertSymbolToInstance(); // we convert to the instance-type
			list_argTypes = new TYPE_LIST(semantic_argType, list_argTypes);
		}

		/***************************************************/
		/* Enter the Function Type to the Symbol Table  */
		/* - We SHOULD first enter the function as a symbol, and only then create its scope */
		/* - This insertion must be done before body.SemantMe inorder to allow recursive calls */
		/***************************************************/
		TYPE_FUNCTION result_SemantMe = new TYPE_FUNCTION(semantic_rtnType, funcName, list_argTypes);
		SYMBOL_TABLE.getInstance().enter(funcName, result_SemantMe);

		return result_SemantMe;
	}

	// SemantMe Part 2: Analyze the inner scope of the function
	private void SemantMe_FuncBody(TYPE_FUNCTION result_SemantMe) {
		/****************************/
		/* Begin Function Scope */
		/****************************/
		// this result is kept in the current scope for the use of STMTs analyzed in the function body
		SYMBOL_TABLE.getInstance().beginScope(TYPE_FOR_SCOPE_BOUNDARIES.FUNC_SCOPE, this, result_SemantMe);

		/***************************/
		/* [2] Add Arguments as local Arguments */
		/***************************/
		TYPE_LIST list_argTypes = result_SemantMe.args;
		for (AST_DEC_FUNC_ARG_LIST it = argList; it  != null; it = it.next)
		{
			// find the TYPE of each
			AST_DEC_FUNC_ARG arg = it.head; // the arg of the iterator
			TYPE argType = list_argTypes.head; // the corresponding type of the arg

			// each argument is also a local variable in the function scope
			// must be done AFTER creating the function scope
			SYMBOL_TABLE.getInstance().enter(arg.argName, argType);

			list_argTypes = list_argTypes.next;
		}

		/*******************/
		/* [3] Semant Body */
		/*******************/
		body.SemantMe();

		// --- Check for return is currently commented-out due to change in instructions ---
		// forces non-void-functions to contain at least one RETURN
		// Must be checked only after body.semantMe;
//		if (!result_SemantMe.isReturnExists && (result_SemantMe.rtnType != null)) {
//			this.throw_error(String.format(">> ERROR no return statement exists, when declared-return is (%s) " +
//					"for function (%s) ", rtnType.type_name, funcName));
//		}

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();
	}

	public TYPE SemantMe()
	{
		// Part 1:
		TYPE_FUNCTION result_SemantMe = SemantMe_FuncSignature();
		// Part 2:
		SemantMe_FuncBody(result_SemantMe);

		return null;
	}

	public boolean isMethod() {
		// NOTE: only relevant AFTER SemantMe
		return encompassingClass != null; // TODO SEMANT IT
	}

}