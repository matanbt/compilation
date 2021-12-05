package AST;

import TYPES.TYPE;

// basically a class to wrap varDec, when it is a statement (=inside func)
public class AST_STMT_VAR_DEC extends AST_STMT
{
    public AST_DEC_VAR d;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/

    public AST_STMT_VAR_DEC(AST_DEC_VAR d, int lineNumber)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== stmt -> varDec ");

        this.d = d;
        this.lineNumber = lineNumber;
    }


    /*****************************************************/
    /* The printing message for a var dec AST node */
    /*****************************************************/
    public void PrintMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST DEC VAR */
        /*************************************/
//        System.out.format("AST NODE STMT VAR DEC\n");

        /****************************************/
        /* RECURSIVELY PRINT type + exp/new_exp ... */
        /****************************************/
        d.PrintMe();


        // we DO NOT add a node here
        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/

        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STMT VAR-DEC"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, d.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        return this.d.SemantMe();
    }
}