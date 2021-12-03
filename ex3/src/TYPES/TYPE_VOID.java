package TYPES;

public class TYPE_VOID extends TYPE
{
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static TYPE_VOID instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TYPE_VOID() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static TYPE_VOID getInstance()
	{
		if (instance == null)
		{
			instance = new TYPE_VOID();
		}
		return instance;
	}

	// we do not allow "void i := ..."
	public boolean canBeAssigned() {
		return false;
	}

	// we forbid void foo() { return nil; }
	public boolean canBeAssignedNil() {
		return false;
	}
}
