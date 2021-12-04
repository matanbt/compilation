package TYPES;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_LIST data_members;

	// the type this L-class defines
	public TYPE_CLASS_INSTANCE classInstanceType;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father, String name, TYPE_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
		this.classInstanceType = new TYPE_CLASS_INSTANCE(name, this);
	}

	public boolean isSubClassOf(TYPE_CLASS potentialFather) {
		return false; // TODO on class task: IMPLEMENT
	}

	// looks for symbol 'name' up the inheritance chain, returns null if not found
	public TYPE_CFIELD find(String name) {
		// look current data members
		// didn't find ---> father.find(name)
		return null;
	}


	public TYPE_CLASS_INSTANCE getInstance() {
		return this.classInstanceType;
	}

	// we forbid assigning to class 'A := nil' when A is a class
	public boolean canBeAssigned() {
		return false;
	}

	public boolean canBeAssignedNil() {
		return false;
	}

	public boolean canBeRtnType() {
		return false; // maybe counter-intuitive, but rtnType should be 'this.classInstanceType'
	}
}
