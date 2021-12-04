package TYPES;

public class TYPE_STRING extends TYPE
{
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static TYPE_STRING instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TYPE_STRING() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static TYPE_STRING getInstance()
	{
		if (instance == null)
		{
			instance = new TYPE_STRING();
			instance.name = "string";
		}
		return instance;
	}

	// we forbid String s := nil;
	public boolean canBeAssignedNil() {
		return false;
	}

	@Override
	public boolean canBeAssigned() {
		return true;
	}

	@Override
	public boolean canBeRtnType() {
		return true;
	}
}
