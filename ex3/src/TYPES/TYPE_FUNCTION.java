package TYPES;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE rtnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST args;

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

	// we forbid the (nasty) case in which 'func i:= func;' where 'func' is a previously declared function
	public boolean canBeAssigned() {
		return false;
	}

	// we do not allow returning functions
	public boolean canBeRtnType() {
		return false;
	}

	// we forbid any assignment to function, in particular the following.
	public boolean canBeAssignedNil() {
		return false;
	}
}
