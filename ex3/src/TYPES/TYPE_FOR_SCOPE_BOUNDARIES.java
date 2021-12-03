package TYPES;

import AST.AST_Node;

public class TYPE_FOR_SCOPE_BOUNDARIES extends TYPE
{
	// ---- names of different scopes ----
	// the first symbol of every program, for the builtins functions / types
	public static String BUILTIN_SCOPE = "BUILTINS-SCOPE";
	// the global scope (comes right after builtins-scope)
	public static String GLOB_SCOPE = "GLOBAL-SCOPE";
	// a scope for function
	public static String FUNC_SCOPE = "FUNCTION-SCOPE";
	// a scope for class
	public static String CLASS_SCOPE = "CLASS-SCOPE";
	// a scope for class
	public static String IF_WHILE_SCOPE = "IF-WHILE-SCOPE";


	// the AST-node / Type representing to this scope (e.g. AST_DEC_FUNC / TYPE_FUNCTION)
	public AST_Node scopeContextAST;
	public TYPE scopeContextType; // could be null, e.g. WHILE Scopes

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FOR_SCOPE_BOUNDARIES(String name, AST_Node scopeContextAST, TYPE scopeContextType)
	{
		this.name = name;
		this.scopeContextAST = scopeContextAST;
		this.scopeContextType = scopeContextType;
	}
}
