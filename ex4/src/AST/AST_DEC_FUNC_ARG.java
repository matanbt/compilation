package AST;

import EXCEPTIONS.SemanticException;
import TYPES.TYPE;


public class AST_DEC_FUNC_ARG extends AST_Node
{
    public AST_TYPE argType;
    public String argName;

    public AST_DEC_FUNC_ARG(AST_TYPE argType, String argName, int lineNumber)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== arg -> argName(%s)\n",argName);

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.argType = argType;
        this.argName = argName;
        this.lineNumber = lineNumber;
    }


    /**************************************************/
    /* The printing message for a simple var AST node */
    /**************************************************/
    public void PrintMe()
    {
        /**********************************/
        /* AST NODE TYPE = AST SIMPLE VAR */
        /**********************************/
        System.out.format("AST NODE FUNCTION ARGUMENT ( %s )\n", argName);

        if (argType != null) argType.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("FUNC-ARG\n(%s)", argName));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, argType.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException {
        return argType.SemantMe();
    }
}
