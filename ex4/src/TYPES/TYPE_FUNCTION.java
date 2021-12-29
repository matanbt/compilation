package TYPES;

import AST.AST_DEC_FUNC;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE rtnType;  // instance-type. null is "the instance" of void

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST args; // instance-types
  
  // ---- Fields updated during semantic analysis ----
	// verify there exist a return (if needed)
	public boolean isReturnExists;
	// for a method it will point on the class it belongs to, otherwise null
	public TYPE_CLASS encompassingClass;

	public AST_DEC_FUNC funcASTNode;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE rtnType,String name,TYPE_LIST args,
						 AST_DEC_FUNC funcASTNode, TYPE_CLASS encompassingClass)
	{
		this.name = name;
		this.rtnType = rtnType;
		this.args = args;
		this.funcASTNode = funcASTNode;
		this.encompassingClass = encompassingClass;

		this.isReturnExists = false;
	}

	public TYPE_FUNCTION(TYPE rtnType,String name,TYPE_LIST args) {
		this(rtnType, name, args, null, null);
	}

	public boolean equals(Object other) {
		if (other == null)
			return false;

		if (! (other instanceof TYPE_FUNCTION))
			return false;

		TYPE_FUNCTION otherFunc = (TYPE_FUNCTION) other;

		// Basic comparison
		if (this == otherFunc) return true;

		// Compare name
		if(!this.name.equals(otherFunc.name)) return false;

		// Compare return type
		if (this.rtnType != otherFunc.rtnType) return false;   // comparing java-instances ids

		// compare args length
		if (this.args.size() != otherFunc.args.size()) return false;

		// Compare args by type args;
		for(int i = 0; i < this.args.size(); i++) {
			if (this.args.get(i) != otherFunc.args.get(i)) return false;  // comparing java-instances ids
		}

		return true;
	}

}
