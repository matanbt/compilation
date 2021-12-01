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
	// in case there is another scope I missed
	public static String OTHER_SCOPE = "CLASS-SCOPE";


	// the node representing to this scope (i.e. AST_DEC_FUNC, AST_DEC_CLASS)
	public AST_Node scopeContext;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FOR_SCOPE_BOUNDARIES(String name, AST_Node scopeContext)
	{
		this.name = name;
		this.scopeContext = scopeContext;
	}

	public TYPE_FOR_SCOPE_BOUNDARIES(String name)
	{
		this(name, null);
	}
}
