package TYPES;

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

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE rtnType,String name,TYPE_LIST args)
	{
		this.name = name;
		this.rtnType = rtnType;
		this.args = args;
		this.isReturnExists = false;
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
