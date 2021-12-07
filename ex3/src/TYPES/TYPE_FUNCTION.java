package TYPES;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE rtnType;  // instance-type. null means void.

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
}
