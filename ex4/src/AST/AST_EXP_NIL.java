package AST;

import IR.*;
import TEMP.*;
import TYPES.TYPE;
import TYPES.TYPE_NIL_INSTANCE;

public class AST_EXP_NIL extends AST_EXP
{
    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_NIL(int lineNumber)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== exp -> NIL\n");

        this.lineNumber = lineNumber;
    }

    /************************************************/
    /* The printing message for a NIL EXP AST node */
    /************************************************/
    public void PrintMe()
    {
        /*******************************/
        /* AST NODE TYPE = AST INT EXP */
        /*******************************/
        System.out.format("AST NODE NIL\n");

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "NIL");
    }
    public TYPE SemantMe()
    {
        return TYPE_NIL_INSTANCE.getInstance();
    }

    public TEMP IRme()
    {
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommandConstInt(t, 0));
        return t;
    }
}
