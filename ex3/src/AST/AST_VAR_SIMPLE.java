package AST;

import EXCEPTIONS.SemanticException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
		this.lineNumber = lineNumber;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}

	public TYPE SemantMe() throws SemanticException {
		SYMBOL_TABLE symbol_table = SYMBOL_TABLE.getInstance();
		TYPE resolved_type = null;

		// Case [1] - We're inside a class definition
		TYPE_CLASS encompassing_class = symbol_table.findScopeClass();
		if (encompassing_class != null) {
			// It must hold that we're inside a method! Otherwise Step-1 will raise null exception

			// Step-1: Look in local variables inside the function
			resolved_type = symbol_table.findWhenInFunctionScope(this.name);
			if (resolved_type != null) return resolved_type;

			// Step-2: Look in class-fields (possibly inherited)
			resolved_type = encompassing_class.findInClassAndSuperClasses(this.name);
			if (resolved_type != null) return resolved_type;

			// Step-3: Look in global scope (meaning perform regular find)
			// Will be done as part of the regular case, after the if
		}

		resolved_type = SYMBOL_TABLE.getInstance().find(this.name);
		if (resolved_type == null) {
			this.throw_error(String.format(">> ERROR:  Failed to resolve variable (%s) ", this.name));
		}

		// a variable must be assignable
		if (!resolved_type.isInstanceOfType()) {
			this.throw_error(String.format(">> ERROR:  got bad type (%s) for variable (%s) ", resolved_type.name, this.name));
		}
		return resolved_type;
	}
}
