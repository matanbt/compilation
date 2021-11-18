package AST;

public class AST_DEC_VAR extends AST_DEC
{
    public AST_TYPE type;
    public String name;
    public AST_EXP exp;
    public AST_NEW_EXP new_exp;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/

    public AST_DEC_VAR(AST_TYPE type, String name, AST_EXP exp, AST_NEW_EXP new_exp)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== varDec -> type ID ");

        if (exp != null)
        {
            System.out.format("ASSIGN exp");
        }
        else if (new_exp != null)
        {
            System.out.format("ASSIGN new_exp");
        }
        System.out.format("\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.type = type;
        this.name = name;
        this.exp = exp;
        this.new_exp = new_exp;
    }

    public AST_DEC_VAR(AST_TYPE type, String name)
    {
        this(type, name, null, null);
    }

    public AST_DEC_VAR(AST_TYPE type, String name, AST_EXP exp)
    {
        this(type, name, exp, null);
    }

    public AST_DEC_VAR(AST_TYPE type, String name, AST_NEW_EXP new_exp)
    {
        this(type, name, null, new_exp);
    }

    /*****************************************************/
    /* The printing message for a var dec AST node */
    /*****************************************************/
    public void PrintMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST DEC VAR */
        /*************************************/
        System.out.format("AST NODE DEC VAR ( %s )\n", name);

        /****************************************/
        /* RECURSIVELY PRINT type + exp/new_exp ... */
        /****************************************/
        type.PrintMe();
        if (exp != null) exp.PrintMe();
        if (new_exp != null) new_exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("VAR-DEC(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
        if (new_exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, new_exp.SerialNumber);
    }
}