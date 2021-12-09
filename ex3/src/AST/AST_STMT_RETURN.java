package AST;

import EXCEPTIONS.SemanticException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_STMT_RETURN extends AST_STMT
{
	public AST_EXP exp;         // could be null (IFF void functions)

	// ---- SEMANTIC FIELDS ----
	public TYPE_FUNCTION inFunc; // the function in which the return is in context

	public AST_STMT_RETURN(AST_EXP exp, int lineNumber)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.print("====================== stmt -> RETURN ");
		if (exp != null) System.out.print(" exp ");
		System.out.print("SEMICOLON\n");

		this.exp = exp;
		this.inFunc = null; // will be defined in the semantic-analysis

		this.lineNumber = lineNumber;
	}

	// overloading contractors:
	public AST_STMT_RETURN(int lineNumber)
	{
		this(null, lineNumber);
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
	public TYPE SemantMe() throws SemanticException {
		// gets the current scope-function
		this.inFunc = SYMBOL_TABLE.getInstance().findScopeFunc();

		// a return for 'inFunc' has been found
		inFunc.isReturnExists = true;

		TYPE expectedReturnType = inFunc.rtnType;
		TYPE actualReturnType = exp != null ? exp.SemantMe(): null;

		// We can look at validating return type as assignment check...
		// The only exception here is that we allow expectedReturnType (=assignee) to be void, which we'll take care separately
		if(expectedReturnType == null) {
			if(actualReturnType != null)
			{
				System.out.format(">> ERROR actual return type (%s) is different than expected (%s)" +
						", in function %s(..)", expectedReturnType.name, actualReturnType.name, inFunc.name);
				// TODO deal with error
				System.exit(0);
			}
			// here if our function is 'void' and our statement is "return;"
			return null; // means success
		}

		// perform regular assignment check for the rest of the cases (non-void functions)
		boolean valid = TYPE.checkAssignment(expectedReturnType, actualReturnType);
		if (!valid) {
			this.throw_error("Return statement");
		}

		return null; // no TYPE for return statement

	}
}
