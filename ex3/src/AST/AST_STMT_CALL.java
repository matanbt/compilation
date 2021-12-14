package AST;

import EXCEPTIONS.SemanticException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_STMT_CALL extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR caller;  // the class's instance who called the function, can be null
	public String func;
	public AST_EXP_LIST args;  // can be null

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_CALL(AST_VAR caller, String func, AST_EXP_LIST args, int lineNumber)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.print("====================== stmt -> "+func+"(); \n");

		this.caller = caller;
		this.func = func;
		this.args = args;
		this.lineNumber = lineNumber;
	}

	// overloading constructors
	public AST_STMT_CALL(String func, int lineNumber)
	{
		this(null, func, null, lineNumber);
	}

	public AST_STMT_CALL(String func, AST_EXP_LIST args, int lineNumber)
	{
		this(null, func, args, lineNumber);
	}

	public AST_STMT_CALL(AST_VAR caller, String func, int lineNumber)
	{
		this(caller, func, null, lineNumber);
	}


	public void PrintMe()
	{
		System.out.print("AST NODE CALL "+func+" STMT\n");

		if (caller != null) caller.PrintMe();
		if (args != null) args.PrintMe();


		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CALL("+ func +")\n");

		if (caller != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, caller.SerialNumber);
		if (args != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, args.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException {
		return this.functionCallSemantMe(caller, func, args);  // instance-type. null if it's a void function call
		// NOTE: by L syntax, this return value is not being used
	}
}
