package AST;

import TYPES.TYPE;
import TYPES.TYPE_NIL;

public class AST_EXP_NIL extends AST_EXP
{
    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_NIL()
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== exp -> NIL\n");
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
    { return TYPE_NIL.getInstance(); }
}
