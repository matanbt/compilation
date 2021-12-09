package TYPES;
/*
        What is Symbol Type?
		EXPLANATION: When we look at the L-code 'int i;', then -
		             'int' is a symbol-type and holds the type TYPE_INT,
		             'i' is a variable and holds the type TYPE_INT_INSTANCE, hence it is NOT a symbol-type.
		EXAMPLE: TYPE_CLASS obviously can be a type in var-decleration, while TYPE_CLASS_INSTANCE can't. A code example:
				 class A {}
				 A obj := new A; // the left 'A' is what we call here a type-symbol, used when declaring a variable with this type.
				 obj notLegal;   // Now this is illegal. That's because TYPE_CLASS_INSTANCE is a special type
				 				 //	which we don't want in declaration, that's why we call it a 'quiet'-type
 */

/*
    An interface for all the symbol-types, e.g. TYPE_CLASS
 */
public interface I_SYMBOL_TYPE {
    // get the type that each instance of 'this' holds
    TYPE getInstanceType();
}
