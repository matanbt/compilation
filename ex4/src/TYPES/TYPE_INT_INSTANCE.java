package TYPES;

public class TYPE_INT_INSTANCE extends TYPE implements I_INSTANCE_TYPE
{
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static TYPE_INT_INSTANCE java_instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TYPE_INT_INSTANCE() {}

	/******************************/
	/* GET SINGLETON JAVA INSTANCE ... */
	/******************************/
	public static TYPE_INT_INSTANCE getInstance()
	{
		if (java_instance == null)
		{
			java_instance = new TYPE_INT_INSTANCE();
			java_instance.name = "int_instance";
		}
		return java_instance;
	}

	@Override
	public TYPE getSymbolType() {
		return TYPE_INT.getInstance();
	}
}
