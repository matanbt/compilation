package AST;

import EXCEPTIONS.SemanticException;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;

	/*******************************************/
	/* The line the node appeared when parsing */
	/*******************************************/
	public int lineNumber;

	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}

	public abstract TYPE SemantMe() throws SemanticException;

	public void throw_error(String message) throws SemanticException {
		throw new SemanticException(String.format("line %d: %s", this.lineNumber, message), this.lineNumber);
	}


	public TYPE functionCallSemantMe(AST_VAR caller, String func, AST_EXP_LIST args) throws SemanticException {
		/* for AST_STMT_CALL, AST_EXP_CALL
		* Verifies that the function call is semantically valid & returns the TYPE of the returned value
		* (from the function call)
		* inputs:
		* caller- the class's instance who called the function, can be null
		* func- the function name
		* args- the function args in the call
		* */

		TYPE type_func;

		if (caller == null){
			TYPE_CLASS type_class_of_scope = SYMBOL_TABLE.getInstance().findScopeClass();
			if (type_class_of_scope == null){
				// this function call is not inside a class scope
				type_func = SYMBOL_TABLE.getInstance().find(func);  // find func in the closest scope  TODO- change to type_func = SYMBOL_TABLE.findInGlobalScope(func);
			}
			else{
				// this function call is inside a class scope
				// search for func in the closest class scope
				type_func = type_class_of_scope.findInClassAndSuperClasses(func);
				if (type_func == null){
					// search for func in the global scope
					type_func = SYMBOL_TABLE.getInstance().find(func);  // TODO- change to type_func = SYMBOL_TABLE.findInGlobalScope(func);
				}
			}
		}

		else{
			// this is a function call by a class instance (caller)
			TYPE caller_type = caller.SemantMe();

			if (! (caller_type instanceof TYPE_CLASS_INSTANCE)){
				this.throw_error("method call by non-instance variable");
			}

			type_func = ((TYPE_CLASS_INSTANCE) caller_type).fromClass.findInClassAndSuperClasses(func);
		}

		if (! (type_func instanceof TYPE_FUNCTION)){
			this.throw_error(String.format("function call: (%s) expected type = function, but got (%s)", func, type_func.name));
		}

		// verify arguments' types
		TYPE_LIST expected_args = ((TYPE_FUNCTION) type_func).args;  // instance-types
		TYPE_LIST type_node;
		AST_EXP_LIST exp_node;
		int i;
		for(type_node = expected_args, exp_node = args, i = 1; type_node != null && exp_node != null;
			type_node = type_node.next, exp_node = exp_node.next, i++) {
			TYPE expected_arg_type = type_node.head;
			TYPE arg_type = exp_node.head.SemantMe();

			boolean valid = TYPE.checkAssignment(expected_arg_type, arg_type);
			if(!valid) {
				this.throw_error(String.format("function call: assigning argument #%d", i));
			}
		}

		if (type_node != null || exp_node != null){
			this.throw_error("function call: unmatching arguments' length");
		}

		return  ((TYPE_FUNCTION) type_func).rtnType;  // instance-type. null if it's a void function call
	}
}
