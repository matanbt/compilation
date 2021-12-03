package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_EXP_CALL extends AST_EXP
{
    public AST_VAR caller;
    public String func;
    public AST_EXP_LIST args;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_CALL(AST_VAR caller, String func, AST_EXP_LIST args)
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
    }

    public AST_EXP_CALL(String func, AST_EXP_LIST args)
    {
        this(null, func, args);
    }

    public AST_EXP_CALL(AST_VAR caller, String func)
    {
        this(caller, func, null);
    }

    public AST_EXP_CALL(String func)
    {
        this(null, func, null);
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

    public TYPE SemantMe()
    {
        TYPE type_func = null;

        if (caller == null){
            type_func = SYMBOL_TABLE.getInstance().find(func);  // find func in the most close scope
        }

        else{
            // find func in the most close class scope
            TYPE caller_type = caller.SemantMe();  // TODO SemantMe for var nodes (in case of class instance- it will be TYPE_CLASS_OBJECT type)

            if (! (caller_type instanceof TYPE_CLASS)){
                System.exit(0);  // TODO- error handling
                return null;
            }

            TYPE_CLASS caller_class = (TYPE_CLASS) caller_type;

            // search for func in caller_class and it's super classes
            while (caller_class != null){
                TYPE_LIST class_data_member_list = caller_class.data_members;
                for(TYPE_LIST node = class_data_member_list; node != null; node = node.next){
                    TYPE class_data_member = node.head;
                    if ((class_data_member instanceof TYPE_FUNCTION) && ((TYPE_FUNCTION) class_data_member).name.equals(func)){
                        type_func = class_data_member;
                        break;
                    }
                }
                // func is not in caller_class, search in it's super class
                caller_class = caller_class.father;
            }

            if (type_func == null){
                // search for func in the global scope
                type_func = SYMBOL_TABLE.findInGlobalScope(func);  // TODO- add findInGlobalScope in SYMBOL_TABLE
            }
        }

        if (! (type_func instanceof TYPE_FUNCTION)){
            System.exit(0);  // TODO- error handling
            return null;
        }

        // verify arguments' types
        TYPE_LIST wanted_args = ((TYPE_FUNCTION) type_func).args;
        TYPE_LIST type_node;
        AST_EXP_LIST exp_node;
        for(type_node = wanted_args, exp_node = args; type_node != null && exp_node != null;
            type_node = type_node.next, exp_node = exp_node.next) {
            TYPE wanted_arg = type_node.head;
            AST_EXP exp = exp_node.head;
            if (exp.SemantMe() != wanted_arg){
                System.exit(0);  // TODO- error handling
                return null;
            }
        }
        return ((TYPE_FUNCTION) type_func).rtnType;
    }
}