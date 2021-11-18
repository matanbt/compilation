package AST;

public class AST_EXP_STRING extends AST_EXP
{
    public String value;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_STRING(String value)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== exp -> String( %s )\n", value);

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.value = value;
    }

    /************************************************/
    /* The printing message for a STRING EXP AST node */
    /************************************************/
    public void PrintMe()
    {
        /*******************************/
        /* AST NODE TYPE = AST INT EXP */
        /*******************************/
        System.out.format("AST NODE STRING( %s )\n",value);

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STRING(%s)",value));
    }
}
