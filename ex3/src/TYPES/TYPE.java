package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... (e.g. 'int') */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}
}
