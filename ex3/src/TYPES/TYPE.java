package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... (e.g. 'int') */
	/******************************/
	public String name;


	/*************/
	/* The following 5 methods SHOULD NOT be overridden
	 (I mean they could, but it's redundant) */
	// TODO make sure we obeyed this before merging to master
	/*************/
	/*************/
	/* isClass() */
	/*************/
	// THIS IS NOT L-CLASS, but the type of instance of one
	public boolean isClass(){ return this instanceof TYPE_CLASS;}

	/*************/
	/* isInstanceOfSomeClass() */
	/*************/
	public boolean isInstanceOfSomeClass(){ return this instanceof TYPE_CLASS_INSTANCE;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}

	/*************/
	/* isFunction() */
	/*************/
	public boolean isFunction(){ return this instanceof TYPE_FUNCTION;}

	/*************/
	/* can an expression of this type be preceded with 'new' keyword? */
	/*************/
	public boolean isNewable() {
		return (isArray() || isClass());
	}

	/*
	 * validate assignment of types: left := right
	 * ALSO general enough to support validation function return statement (keep it that way)
	 *
	 * TRUE means the assignment is valid
	 * In case of error it returns 'FALSE' and print some informative message. We DO NOT handle errors here.
	 */
	public static boolean checkAssignment(TYPE left, TYPE right) {

		if(!left.canBeAssigned()) {
			// we note that canBeVarType holds for 'left' IFF it can be assigned with some value
			System.out.format(">> ERROR type (%s) cannot be used as an assignee\n", left.name);
			return false;
		}

		if (!left.canBeAssignedNil() && right == TYPE_NIL.getInstance()) {
			System.out.format(">> ERROR cannot assign NIL to type (%s)\n", left.name);
			return false;
		}

		else if ((left != right) && (right != TYPE_NIL.getInstance())) {
			if (right.isInstanceOfSomeClass() && left.isInstanceOfSomeClass()) {
				// in OOP we allow assignment of different types
				if(!((TYPE_CLASS_INSTANCE) right).isSubClassOf((TYPE_CLASS_INSTANCE) left)) // TODO after TYPE_CLASS implementation
				{
					System.out.format(">> ERROR:  cannot assign to class (%s) is" +
							" NOT super class of (%s)", left.name, right.name);
					return false;
				}
			}
			else {
				// OOP assignment failed means error
				System.out.format(">> ERROR: expected assigned type of (%s) " +
						"but got (%s) \n", left.name, right.name);
				return false;
			}
		}
		return true;
	}


	/**************************  Properties of TYPE  ************************************/
	/* The logic behind the following 3 functions is that each type can be
	used as a return-type and a variable-type, unless else is stated explicitly by
	overriding functions below.  */
	/**********************************************************************************/
	/* Can this type used as a type returned from a function (i.e. in its signature)?
	 * EXAMPLE: TYPE_NIL.getInstance().canBeRtnType() == false
	 * DEFAULT: is true - must be overridden to change this fact
	 */
	public boolean canBeRtnType() { return true; }

	/*************/
	/* Can this type be assigned with value (i.e. in its declaration)?
	 * This allows us to distinguish between identifiers that can be assigned
	 * For example TYPE_VOID.getInstance().canBeRtnType() == false
	 * NOTE: this check is equivalent to the question "can this type be declared for a variable?"
	 *       for example - a function cannot be assigned, and cannot be variable
	 * DEFAULT: is true - must be overridden to change this fact */
	public boolean canBeAssigned() { return true; }

	/*************/
	/* Can this type be assigend with 'nil'?
	   EXAMPLE: TYPE_INT.getInstance().canBeAssignedNil == false
	   DEFAULT: is the same as 'canBeAssigned'
	 */
	public boolean canBeAssignedNil() { return canBeAssigned(); }
}
