package AST;

public class AST_FUNC_ARG extends AST_NODE
{
    public AST_TYPE argType;
    public String argName;

    public AST_FUNC_ARG(AST_TYPE argType, String argName)
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
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.argType = argType;
        this.argName = argName;
    }

    /**************************************************/
    /* The printing message for a simple var AST node */
    /**************************************************/
    public void PrintMe()
    {
        /**********************************/
        /* AST NODE TYPE = AST SIMPLE VAR */
        /**********************************/
        System.out.format("AST NODE FUNCTION ARGUMENT ( %s )\n",argName);

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
}
