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

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE rtnType,String name,TYPE_LIST args)
	{
		this.name = name;
		this.rtnType = rtnType;
		this.args = args;
	}
}
