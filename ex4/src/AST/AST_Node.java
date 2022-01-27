package AST;

import EXCEPTIONS.SemanticException;

import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;

import java.util.ArrayList;
import java.util.List;

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
	public final IR mIR = IR.getInstance(); // IR instance to be used in IRme-s

	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}

	public abstract TYPE SemantMe() throws SemanticException;

	/*****************************************/
	/* The default IR action for an AST node */
	/*****************************************/
	public TEMP IRme()
	{
		return null;
	}

	public void throw_error(String message) throws SemanticException {
		throw new SemanticException(String.format("line %d: %s", this.lineNumber, message), this.lineNumber);
	}

/* ---------------------------- Function Calls Section ----------------------------*/
	public TYPE_FUNCTION functionCallSemantMe(AST_VAR caller, String func, AST_EXP_LIST args) throws SemanticException {
		/* for AST_STMT_CALL, AST_EXP_CALL
		* Verifies that the function call is semantically valid & returns the TYPE of the returned value
		* (from the function call)
		* inputs:
		* caller- the class's instance who called the function, can be null
		* func- the function name
		* args- the function args in the call
		* */

		TYPE type_func;

		if (caller == null) {
			TYPE_CLASS type_class_of_scope = SYMBOL_TABLE.getInstance().findScopeClass();
			if (type_class_of_scope == null) {
				// this function call is not inside a class scope
				type_func = SYMBOL_TABLE.getInstance().find(func);  // find func in the closest scope  TODO- (enhancement) change to type_func = SYMBOL_TABLE.findInGlobalScope(func);
			}
			else {
				// this function call is inside a class scope
				// search for func in most local scope
				type_func = SYMBOL_TABLE.getInstance().findInCurrentScope(func);
				if (type_func == null) {
					// search for func in the closest class scope
					type_func = type_class_of_scope.findInClassAndSuperClasses(func);
				}
				if (type_func == null){
					// search for func in the global scope
					type_func = SYMBOL_TABLE.getInstance().find(func);  // TODO- (enhancement) change to type_func = SYMBOL_TABLE.findInGlobalScope(func);
				}
			}
		}

		else {
			// this is a function call by a class instance (caller)
			TYPE caller_type = caller.SemantMe();

			if (! (caller_type instanceof TYPE_CLASS_INSTANCE)) {
				this.throw_error("method call by non-instance variable");
			}

			type_func = ((TYPE_CLASS_INSTANCE) caller_type).fromClass.findInClassAndSuperClasses(func);
		}

		if (type_func == null)
		{
			this.throw_error(String.format("function call: (%s) undefined", func));
		}
		if (! (type_func instanceof TYPE_FUNCTION)) {
			this.throw_error(String.format("function call: (%s) expected type = function, but got (%s)", func, type_func.name));
		}

		// verify arguments' types
		TYPE_LIST expected_args = ((TYPE_FUNCTION) type_func).args;  // instance-types
		AST_EXP_LIST exp_node;
		int i;

		for(exp_node = args, i = 0; exp_node != null && i < expected_args.size();
			exp_node = exp_node.next, i++) {

			TYPE expected_arg_type = expected_args.get(i);
			TYPE arg_type = exp_node.head.SemantMe();  // Notice: can return null

			boolean valid = TYPE.checkAssignment(expected_arg_type, arg_type);
			if(!valid) {
				this.throw_error(String.format("function call: assigning argument #%d", (i + 1)));
			}
		}

		if (i != expected_args.size() || exp_node != null) {
			this.throw_error("function call: unmatching arguments' length");
		}

		return  ((TYPE_FUNCTION) type_func);
	}

	/*
	 * Inserts the IRcommands of a function / method call
	 */
	public void functionCallIRme(AST_VAR caller, String func, AST_EXP_LIST args,
								 TYPE_FUNCTION funcType, TEMP rtnTemp) {
		List<TEMP> args_temp_list = null;

		if (funcType.encompassingClass != null) {
			/* Calling a method (Note: the encompassingClass was captured in the function signature)*/

			// the class-instance that invokes the method
			TEMP invokingClassObject;

			if (caller == null) {
				/* calling a method without a caller implies the method was called
				 * on the `this` of the current runtime context. */
				invokingClassObject = TEMP_FACTORY.getInstance().getFreshTEMP();
				IR.getInstance().Add_IRcommand(new IRcommand_Load(invokingClassObject , IDVariable.getThisInstance()));
			}
			else {
				/* there is an explicit class-object caller */
				/* todo: I assume caller.IRme will provide me with the address of the object.
				    this depends on new-exp implementation */
				invokingClassObject = caller.IRme();
			}

			// evaluating expressions must be AFTER evaluating invokingClassObject (left to right)
			args_temp_list = this.functionCallGetArgumentsTempList(args);
			args_temp_list.add(0, invokingClassObject); // invoking object is the first argument

			/* TODO Offset - we'll need to pass the method offset in VTable */
			IR.getInstance().Add_IRcommand(new IRcommand_Virtual_Call(invokingClassObject,
					funcType.encompassingClass.getMethodOffset(func),
					args_temp_list, rtnTemp));
		}
		else {
			/* Calling a global function
			 * By semantic assumption caller is null */

			args_temp_list = this.functionCallGetArgumentsTempList(args);

			/* Handling built-in functions here*/
			switch (func) {
				case "PrintInt":
					mIR.Add_IRcommand(new IRcommand_PrintInt(args_temp_list.get(0)));
					break;
				case "PrintString":
					mIR.Add_IRcommand(new IRcommand_PrintString((args_temp_list.get(0))));
					break;
				default:
					mIR.Add_IRcommand(new IRcommand_Call(func, args_temp_list, rtnTemp));
					break;
			}
		}
	}

	/* Performs IRme to all the expressions in the given list, and the corresponding list of TEMPs*/
	public List<TEMP> functionCallGetArgumentsTempList(AST_EXP_LIST args) {
		AST_EXP_LIST exp_node;
		List<TEMP> args_temp_list = new ArrayList<TEMP>();

		// IRing the expression from left to right
		for(exp_node = args; exp_node != null; exp_node = exp_node.next) {
			AST_EXP arg = exp_node.head;
			TEMP argTemp = arg.IRme();
			args_temp_list.add(argTemp);
		}

		return args_temp_list;
	}
	/* ---------------------------- END Function Calls Section ----------------------------*/

}
