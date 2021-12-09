package TYPES;

public class TYPE_STRING_INSTANCE extends TYPE implements I_INSTANCE_TYPE
{
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static TYPE_STRING_INSTANCE instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TYPE_STRING_INSTANCE() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static TYPE_STRING_INSTANCE getInstance()
	{
		if (instance == null)
		{
			instance = new TYPE_STRING_INSTANCE();
			instance.name = "string_instance";
		}
		return instance;
	}

	@Override
	public TYPE getSymbolType() {
		return TYPE_STRING.getInstance();
	}
}
