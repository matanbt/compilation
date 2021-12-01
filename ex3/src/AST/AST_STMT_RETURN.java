package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_STMT_RETURN extends AST_STMT
{
	public AST_EXP exp;         // could be null (IFF void functions)
	public AST_DEC_FUNC inFunc; // the function in which the return is in context

	public AST_STMT_RETURN(AST_EXP exp)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.print("====================== stmt -> RETURN ");
		if (exp != null) System.out.print(" exp ");
		System.out.print("SEMICOLON\n");

		this.exp = exp;
		this.inFunc = null; // will be defined in the semantic-analysis
	}

	// overloading contractors:
	public AST_STMT_RETURN()
	{
		this(null);
	}


	public void PrintMe()
	{
		System.out.print("AST NODE RETURN STMT\n");
		if (exp != null) exp.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"RETURN");

		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
	}

	/* verifies the type we return is the same as in the function declaration */
	public TYPE SemantMe() {
		// gets the current scope-function
		this.inFunc = SYMBOL_TABLE.getInstance().findScopeFunc();

		TYPE expectedReturnType = inFunc.result_SemantMe.rtnType;
		TYPE actualReturnType = exp != null ? exp.SemantMe(): TYPE_VOID.getInstance();

		if(actualReturnType == TYPE_NIL.getInstance()) {
			// "return null" - is not allowed for int, string, void
			if(expectedReturnType == TYPE_INT.getInstance()
					|| expectedReturnType == TYPE_STRING.getInstance()
					|| expectedReturnType == TYPE_VOID.getInstance()) {

				System.out.format(">> ERROR - return null is not allowed for primitive types");
				// TODO deal with error
				System.exit(0);
			}
		}

		if(expectedReturnType != actualReturnType){
			// TODO make sure that we have a type factory, so this equal will work (i.e. I assume each type is an instance!)

			System.out.format(">> ERROR actual return type (%s) is different than expected (%s)" +
					", in function %s()", expectedReturnType.name, actualReturnType.name, inFunc.funcName);
			// TODO deal with error
			System.exit(0);
		}

		return null; // no TYPE for return statement

	}
}
