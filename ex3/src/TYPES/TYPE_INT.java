package TYPES;

import SYMBOL_TABLE.SYMBOL_TABLE;

public class TYPE_INT extends TYPE implements I_SYMBOL_TYPE
{
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static TYPE_INT java_instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TYPE_INT() {}

	/******************************/
	/* GET SINGLETON JAVA INSTANCE ... */
	/******************************/
	public static TYPE_INT getInstance()
	{
		if (java_instance == null)
		{
			java_instance = new TYPE_INT();
			java_instance.name = "int";
		}
		return java_instance;
	}

	@Override
	public TYPE getInstanceType() {
		return TYPE_INT_INSTANCE.getInstance();
	}
}
