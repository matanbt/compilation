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
		if (! (other instanceof TYPE_FUNCTION))
			return false;

		TYPE_FUNCTION otherFunc = (TYPE_FUNCTION) other;

		// Basic comparison
		if (this == otherFunc) return true;

		// Compare name
		if(!this.name.equals(otherFunc.name)) return false;

		// Compare return type
		if (this.rtnType != otherFunc.rtnType) return false;   // comparing java-instances ids

		// Compare args by type
		TYPE_LIST thisNode = this.args;
		TYPE_LIST otherNode = otherFunc.args;

		for(; thisNode != null && otherNode != null;
			thisNode = thisNode.next, otherNode = otherNode.next) {
			TYPE thisArg = thisNode.head;
			TYPE otherArg = otherNode.head;
			if (thisArg != otherArg) return false;  // comparing java-instances ids
		}

		if (thisNode != null || otherNode != null){
			// unmatching arguments length
			return false;
		}

		return true;
	}

}
