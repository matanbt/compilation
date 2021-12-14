package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... (e.g. 'int') */
	/******************************/
	public String name; // this is the identifier in the L-Code that represents the TYPE

	/*************/
	/* The following methods SHOULD NOT be overridden
	 (I mean they could, but it's redundant) */
	/*************/
	/*************/
	/* isClass() */
	/*************/
	// THIS IS NOT L-CLASS, but the type of instance of one
	public boolean isClassSymbol(){ return this instanceof TYPE_CLASS;}

	/*************/
	/* isInstanceOfSomeClass() */
	/*************/
	public boolean isInstanceOfSomeClass(){ return this instanceof TYPE_CLASS_INSTANCE;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArraySymbol(){ return this instanceof TYPE_ARRAY;}

	/*************/
	/* isInstanceOfSomeClass() */
	/*************/
	public boolean isInstanceOfSomeArray(){ return this instanceof TYPE_ARRAY_INSTANCE;}
	/*************/
	/* isFunction() */
	/*************/
	public boolean isFunction(){ return this instanceof TYPE_FUNCTION;}



	/**************************  Properties of TYPE  ************************************/
	/* The following functions will define the properties of each TYPE
	------ DO NOT OVERRIDE THESE : WE USED THEM AS METHOD FOR SIMPLICITY OF USE ------
	/**********************************************************************************/
	/*
		Can this type be used for as a type-symbol?
		For this collection of types, the property TYPE.name stands for the identifier used when declaring the TYPE (e.g. TYPE_INT.name == 'int')
	 */

	public boolean isSymbolType() {
		// We DO NOT consider void as Symbol-Type, so extra care is done in AST_DEC_FUNC
		return this instanceof I_SYMBOL_TYPE;
	}

	public boolean isInstanceOfType() {
		return this instanceof I_INSTANCE_TYPE;
	}

	/*************/
	/* Can this type be assigned with value (i.e. in its declaration)?
	 * This allows us to distinguish between identifiers that can be assigned
	 * For example TYPE_VOID.getInstance().canBeAssigned() == false
	 * EXAMPLE: we forbid the (nasty) case in which 'func i:= func;' where 'func' is a previously declared function
	 * EXAMPLE: `int num;` num.semantMe().canBeAssigned() == true
	 * NOTE: this check is equivalent to the question "can this type be declared for a variable?"
	 *       for example - a function cannot be assigned, and cannot be variable */
	public boolean canBeAssigned() { return isInstanceOfType(); }

	/*************/
	/* Can this type be assigned with 'nil'?
	   EXAMPLE: TYPE_INT.getInstance().canBeAssignedNil == false
	 */
	public boolean canBeAssignedNil() { return this instanceof TYPE_CLASS_INSTANCE || this instanceof TYPE_ARRAY_INSTANCE; }



	//****************************************************************
	// ************************** HELPERS ****************************
	//****************************************************************
	public TYPE convertSymbolToInstance() {
		assert isSymbolType();
		return ((I_SYMBOL_TYPE) this).getInstanceType();
	}

	public TYPE convertInstanceToSymbol() {
		assert isInstanceOfType();
		return ((I_INSTANCE_TYPE) this).getSymbolType();
	}

	/*
	 * validate assignment of types: left := right
	 * ALSO general enough to support validation function return statement (keep it that way)
	 *
	 * TRUE means the assignment is valid
	 * In case of error it returns 'FALSE' and print some informative message. We DO NOT handle errors here.
	 */
	public static boolean checkAssignment(TYPE left, TYPE right) {

		if(left == null || right == null) {
			// null usually represent void, and we do not allow void assignment in both sides
			System.out.format(">> ERROR cannot assign void / to void \n");
			return false;
		}

		if(!left.canBeAssigned()) {
			// we note that canBeVarType holds for 'left' IFF it can be assigned with some value
			System.out.format(">> ERROR type (%s) cannot be used as an assignee\n", left.name);
			return false;
		}

		if (!left.canBeAssignedNil() && right == TYPE_NIL_INSTANCE.getInstance()) {
			System.out.format(">> ERROR cannot assign NIL to type (%s)\n", left.name);
			return false;
		}
		else if ((left != right) && (right != TYPE_NIL_INSTANCE.getInstance())) {
			if (right.isInstanceOfSomeClass() && left.isInstanceOfSomeClass()) {
				// in OOP we allow assignment of different types
				if(!((TYPE_CLASS_INSTANCE) right).isSubClassOf((TYPE_CLASS_INSTANCE) left))
				{
					System.out.format(">> ERROR:  cannot assign to class (%s) is" +
							" NOT super class of (%s)\n", left.name, right.name);
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

	/*
		Checks assignments of sort 'IntArray arr := new int[5]'.
	 */
	public static boolean checkNewArrayAssignment(TYPE left, TYPE_ARRAY_INSTANCE right) {
		TYPE rightElementTypeIns = right.getElementType().convertSymbolToInstance();

		if(!(left instanceof TYPE_ARRAY_INSTANCE)) {
			System.out.println("Can't assign new array to non-array type");
			return false;
		}

		TYPE leftElementTypeIns = ((TYPE_ARRAY_INSTANCE)left).getElementType().convertSymbolToInstance();
		if (leftElementTypeIns instanceof TYPE_CLASS_INSTANCE
				&& rightElementTypeIns instanceof TYPE_CLASS_INSTANCE)
		{
			// forbid any case in which inheritance doesn't hold.
			if (!((TYPE_CLASS_INSTANCE) rightElementTypeIns).isSubClassOf((TYPE_CLASS_INSTANCE) leftElementTypeIns)) {
				System.out.format("No inheritance relation between (%s) and (%s)\n",
						leftElementTypeIns, rightElementTypeIns);
				return false;
			}

			// allows inheritance when assigning new array
			return true;
		}

		if (leftElementTypeIns != rightElementTypeIns) {
			System.out.format("Expected new array of (%s), but got (%s)\n",
					leftElementTypeIns, rightElementTypeIns);
			return false;
		}
		return true;
	}

}
