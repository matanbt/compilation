package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_VAR extends AST_DEC
{
    public AST_TYPE type;
    public String name;
    public AST_EXP exp;
    public AST_NEW_EXP new_exp;

    // ---- Semantic Properties ---
    public boolean isCField = false; // TODO - should be marked by CField

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
        // NOTE either exp or new_exp must be null
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


    public TYPE SemantMe()
    {
        TYPE semantic_type;

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        semantic_type = this.type.SemantMe();
        if (semantic_type == null)
        {
            System.out.format(">> ERROR non existing type (%s)\n", this.type);
            // TODO ERROR HANDLING
            System.exit(0);
        }
        if (semantic_type.isClass()) {
            // we want our declared variable to be INSTANCE of the class
            semantic_type = ((TYPE_CLASS) semantic_type).getInstance();
        }
        if (!semantic_type.canBeAssigned()) {
            System.out.format(">> ERROR type (%s) cannot be used as a variable type\n", this.type);
            // TODO ERROR HANDLING
            System.exit(0);
        }

        /**************************************/
        /* [2] Check That Name does NOT exist */
        /**************************************/
        if(isCField) {
            // CField is being checked separately by AST_CFEILD
        }
        // else means it's scope declaration (might be global)
        else if (SYMBOL_TABLE.getInstance().findInCurrentScope(name) != null)
        {
            System.out.format(">> ERROR variable %s already exists inside the scope\n", name);
            // TODO deal with error
            System.exit(0);
        }

        /***************************************************/
        /* [3] Check for the given value is from expected type */
        /***************************************************/
        TYPE valueType = null;
        if (exp != null) {
            valueType = exp.SemantMe();
        }
        else if (new_exp != null) {
            valueType = new_exp.SemantMe();
        }

        if (valueType != null) { // means there is an assignment
            TYPE.checkAssignment(semantic_type, valueType, name);
        }
        /***************************************************/
        /* [4] Enter the Function Type to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(name, semantic_type);

        /*********************************************************/
        /* [5] Return value is irrelevant for declarations */
        /*********************************************************/
        return null;
    }

}