package AST;

import EXCEPTIONS.SemanticException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_EXP_CALL extends AST_EXP
{
    public AST_VAR caller;  // the class's instance who called the function, can be null
    public String func;
    public AST_EXP_LIST args;  // can be null

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_CALL(AST_VAR caller, String func, AST_EXP_LIST args, int lineNumber)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== exp -> ");
        if (caller != null)
        {
            System.out.format("var.");
        }
        System.out.format("ID(");
        if (args != null)
        {
            System.out.format("exp [, exp]*");
        }
        System.out.format(")\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.caller = caller;
        this.func = func;
        this.args = args;
        this.lineNumber = lineNumber;
    }

    public AST_EXP_CALL(String func, AST_EXP_LIST args, int lineNumber)
    {
        this(null, func, args, lineNumber);
    }

    public AST_EXP_CALL(AST_VAR caller, String func, int lineNumber)
    {
        this(caller, func, null, lineNumber);
    }

    public AST_EXP_CALL(String func, int lineNumber)
    {
        this(null, func, null, lineNumber);
    }

    /*****************************************************/
    /* The printing message for a function call exp AST node */
    /*****************************************************/
    public void PrintMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST EXP CALL */
        /*************************************/
        System.out.format("AST NODE EXP CALL ( %s )\n", func);

        /****************************************/
        /* RECURSIVELY PRINT caller + args ... */
        /****************************************/
        if (caller != null) caller.PrintMe();
        if (args != null) args.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        String msg = null;
        if (caller != null)
        {
            msg = String.format("...CALL(%s)(...)", func);
        }
        else
        {
            msg = String.format("CALL(%s)(...)", func);
        }
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                msg);
        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (caller != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, caller.SerialNumber);
        if (args != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, args.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException {
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
        TYPE return_type = ((TYPE_FUNCTION) type_func).rtnType;  // instance-type. null if it's a void function call
        if (return_type == null)
            this.throw_error("void function was called but a return value is expected (EXP_CALL)");
        return return_type;
    }
}