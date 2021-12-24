package AST;

import EXCEPTIONS.SemanticException;
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
        return this.functionCallSemantMe(caller, func, args);  // instance-type. null if it's a void function call
    }

    public TEMP IRme()
    {
        TEMP t=null;

        if (params != null) { t = params.head.IRme(); }

        IR.getInstance().Add_IRcommand(new IRcommand_PrintInt(t));

        return null;
    }
}