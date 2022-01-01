package AST;

import EXCEPTIONS.SemanticException;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;
import MIPS.*;

public class AST_DEC_FUNC extends AST_DEC
{
	public AST_TYPE rtnType;
	public String funcName;
	public AST_STMT_LIST body;
	public AST_DEC_FUNC_ARG_LIST argList; // could be null

	// -------------------- Semantic Additions --------------------
	// non-null means method of 'encompassingClass', and null means it's global function
	public TYPE_CLASS encompassingClass = null;
	public TYPE funcType = null;  // gets real value when calling getType

	// -------------------- IR Additions --------------------
	private int argsCount = 0;
	public int localsCount = 0;
	public String funcStartingLabel;
	public String funcEpilogueLabel;
	public boolean isMainFunc = false;
	public static boolean isFoundMain = false;

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
		/* NOTE: Assumes declaration is in one line!!! */
		this.lineNumber = rtnType.lineNumber;
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
		TYPE_CLASS encompassingClass = SYMBOL_TABLE.getInstance().findScopeClass();


		TYPE semantic_rtnType = null; // the actual instance-type we expect to get in return statement, although we allow void
		if(semantic_rtnType_signature != null) {
			semantic_rtnType = semantic_rtnType_signature.convertSymbolToInstance();
		}

		/*******************/
		/* Check that func name is valid */
		/*******************/
		if(encompassingClass == null) {
			// we're in global context, so we make sure no same-name declaration
			if(SYMBOL_TABLE.getInstance().find(funcName) != null) {
				this.throw_error(String.format( "identifier (%s) is previously declared, " +
						"can't declare global-function", funcName));
			}
		}
		else{
			// it's a CField
			// annotation for IR
			encompassingClass.addToMethodList(this);
			// CField semantic check is on AST_CFEILD
		}


		/***************************/
		/* [2] Semant Arguments */
		/***************************/
		TYPE_LIST list_argTypes = new TYPE_LIST(); // lists of the semantic types of arguments
		for (AST_DEC_FUNC_ARG_LIST it = argList; it  != null; it = it.next)
		{
			// find the TYPE of each
			AST_DEC_FUNC_ARG arg = it.head; // the arg of the iterator
			TYPE semantic_argType = arg.SemantMe();
			if (semantic_argType == null)
			{
				this.throw_error(String.format("non existing type %s",arg.argType));
			}
			// each argument has a type that we'll want to verify when it'll be called
			semantic_argType = semantic_argType.convertSymbolToInstance(); // we convert to the instance-type
			list_argTypes.add(semantic_argType);
		}

		/*******************************************/
		/* [3] Handling the case of global main() */
		/******************************************/
		if(encompassingClass == null && funcName.equals("main")) {
			if (semantic_rtnType != null)
			{
				this.throw_error("Global declared 'main()' must be void");
			}
			if (list_argTypes.size() != 0)
			{
				this.throw_error("Global declared 'main()' can't have arguments");
			}

			// flag the function as the legal "main()" of the program
			this.isMainFunc = true;
			// flag that the main function was found
			isFoundMain = true;
		}

		return new TYPE_FUNCTION(semantic_rtnType, funcName, list_argTypes, this);
	}

	// SemantMe Part 2: Update Symbol Table & analyze the inner scope of the function
	private void SemantMe_FuncBody(TYPE_FUNCTION result_SemantMe) throws SemanticException {
		/***************************************************/
		/* Enter the Function Type to the Symbol Table  */
		/* - We SHOULD first enter the function as a symbol, and only then create its scope */
		/* - This insertion must be done before body.SemantMe inorder to allow recursive calls */
		/* - This insertion must be done after AST_CFIELD name and signature check (in the method-cfield case) */
		/***************************************************/
		SYMBOL_TABLE.getInstance().enter(funcName, result_SemantMe);

		/****************************/
		/* Begin Function Scope */
		/****************************/
		// this result is kept in the current scope for the use of STMTs analyzed in the function body
		SYMBOL_TABLE.getInstance().beginScope(TYPE_FOR_SCOPE_BOUNDARIES.FUNC_SCOPE, this, result_SemantMe);

		/***************************/
		/* [2] Add Arguments as local Arguments */
		/***************************/
		TYPE_LIST list_argTypes = result_SemantMe.args;
		int i = 0;
		for (AST_DEC_FUNC_ARG_LIST it = argList; it  != null; it = it.next, i++)
		{
			// find the TYPE of each
			AST_DEC_FUNC_ARG arg = it.head; // the arg of the iterator
			TYPE argType = list_argTypes.get(i); // the corresponding type of the arg

			// each argument is also a local variable in the function scope
			// must be done AFTER creating the function scope
			SYMBOL_TABLE.getInstance().enter(arg.argName, argType,
					new IDVariable(arg.argName, VarRole.ARG, i));
		}

		// gets the amount of arguments
		this.argsCount = list_argTypes.size();

		/*******************/
		/* [3] Semant Body */
		/*******************/
		this.localsCount = 0; // locals will be count by body.semantMe()
		// TODO this reasonably assumes each STMT_VAR_DEC invokes this.localsCount++ (and only these statements do)

		body.SemantMe();

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();
	}

	public TYPE SemantMe() throws SemanticException {
		SemantMe_FuncBody((TYPE_FUNCTION) getType());
		return null;  // Return value is irrelevant for declarations
	}

	public TYPE getType() throws SemanticException {
		// getType() isn't changing the Symbol Table & not entering the function scope
		if (this.funcType == null)
			this.funcType = SemantMe_FuncSignature();
		return this.funcType;
	}

	public String getName(){
		return this.funcName;
	}


	public TEMP IRme()
	{
		/* IR speaking - I assume the functions arguments are loaded
		 * if thinking about MIPS - This means I assume the caller pushed the arguments to the stack */

		/* 0. If it's the global main(): Rename it */
		String _funcName = this.isMainFunc ? "user_main" : this.funcName;

		/* 1. Put a starting label */
		if (encompassingClass != null) {
			this.funcStartingLabel = String.format("method_%s_%s", encompassingClass.name, _funcName);
		}
		else {
			this.funcStartingLabel = String.format("func_%s", _funcName);
		}
		mIR.Add_IRcommand(new IRcommand_Label(this.funcStartingLabel));

		/* 2. Prologue (Will be used for MIPS, meaningless in IR) */
		mIR.Add_IRcommand(new IRcommand_Func_Prologue(this.localsCount));

		/* 3. Function body */
		if (body != null) body.IRme();

		/* 4. Epilogue (Will be used for MIPS, meaningless in IR) */
		this.funcEpilogueLabel = String.format("%s_epilogue", this.funcStartingLabel);
		mIR.Add_IRcommand(new IRcommand_Label(this.funcEpilogueLabel));
		mIR.Add_IRcommand(new IRcommand_Func_Epilogue());

		// No temporary in function declaration
		return null;
	}

}