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

	public static boolean isClassInstance(TYPE t) {
		return t != TYPE_INT.getInstance()
				&& t != TYPE_STRING.getInstance()
				&& !(t instanceof TYPE_ARRAY);
	}

	/*
	 * validate assignment of types: left := right
	 * Handles error in case of encountering one
	 */
	public static void checkAssignment(TYPE left, TYPE right, String assigneeName) {
		if (right == TYPE_NIL.getInstace()
				&& (left == TYPE_INT.getInstance() || left == TYPE_STRING.getInstance())) {
			System.out.format(">> ERROR cannot assign NIL to primitive types (int, string)\n", assigneeName);
			// TODO deal with error
			System.exit(0);
		}
		else if (right != TYPE_NIL.getInstace() && right != left) {
			if (TYPE.isClassInstance(right) && TYPE.isClassInstance(left)) {
				// in OOP we allow assignment of different types
				if(!((TYPE_CLASS) right).isSubClassOf((TYPE_CLASS) left)) // TODO after TYPE_CLASS implementation
				{
					System.out.format(">> ERROR in declaration of (%s) :  cannot assign to class (%s) is" +
							" NOT super class of (%s)", assigneeName, left.name, right.name);
					// TODO deal with error
					System.exit(0);
				}
			}
			else {
				// OOP assignment failed means error
				System.out.format(">> ERROR in declaration of (%s) : expected assigned type of (%s) " +
						"but got (%s) \n", assigneeName, left.name, right.name);
				// TODO deal with error
				System.exit(0);
			}
		}
	}
}
