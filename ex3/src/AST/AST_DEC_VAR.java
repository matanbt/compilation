package AST;

import EXCEPTIONS.SemanticException;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_VAR extends AST_DEC
{
    public AST_TYPE type;
    public String name;
    public AST_EXP exp;
    public AST_NEW_EXP new_exp;

    // ---- Semantic Properties ---
    // non-null means var field of 'encompassingClass', and null means it's not in class scope
    public TYPE_CLASS encompassingClass = null;
    public TYPE varType = null;  // gets real value when calling getType

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/

    public AST_DEC_VAR(AST_TYPE type, String name, AST_EXP exp, AST_NEW_EXP new_exp, int lineNumber)
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
        this.lineNumber = lineNumber;
    }

    public AST_DEC_VAR(AST_TYPE type, String name, int lineNumber)
    {
        this(type, name, null, null, lineNumber);
    }

    public AST_DEC_VAR(AST_TYPE type, String name, AST_EXP exp, int lineNumber)
    {
        this(type, name, exp, null, lineNumber);
    }

    public AST_DEC_VAR(AST_TYPE type, String name, AST_NEW_EXP new_exp, int lineNumber)
    {
        this(type, name, null, new_exp, lineNumber);
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

    // SemantMe Part 1: Analyze the var type by it's declared type
    private TYPE SemantMe_declaredType() throws SemanticException {
        TYPE semantic_type;

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        semantic_type = this.type.SemantMe();
        if (semantic_type == null)
        {
            this.throw_error(String.format("non existing type (%s)\n", this.type));
        }

        semantic_type = semantic_type.convertSymbolToInstance();

        /**************************************/
        /* [2] Check That Name does NOT exist */
        /**************************************/
        if (this.encompassingClass == null) {
            // it's a scope declaration (might be global)
            if (SYMBOL_TABLE.getInstance().findInCurrentScope(name) != null) {
              this.throw_error(String.format("variable %s already exists inside the scope\n", name));
            }
        }
        // else - CField is being checked separately by AST_CFEILD

        return semantic_type;
    }


    // SemantMe Part 2: Check assignment (if exists) & update Symbol Table
    private void SemantMe_checkAssignment(TYPE semantic_type) throws SemanticException {
        /*
        * semantic_type = instance type representing the type of var by it's type declaration
        * e.g.: 'int x := "";'  --> semantic_type = TYPE_INT_INSTANCE
        * */

        /***************************************************/
        /* [1] Check that the given value is from expected type */
        /***************************************************/
        TYPE valueType = null;
        if (exp != null) {
            valueType = exp.SemantMe();
        }
        else if (new_exp != null) {
            valueType = new_exp.SemantMe();
        }
        if (valueType == null && (exp != null || new_exp != null)) {
            // got null from semantMe
            this.throw_error("illegal assignment for the var-declaration");
        }

        if (valueType != null) { // means there is an assignment
            boolean valid = true;

            if (new_exp != null && valueType instanceof TYPE_ARRAY_INSTANCE) {
                // Special case of creating a new array instance
                valid = TYPE.checkNewArrayAssignment(semantic_type, (TYPE_ARRAY_INSTANCE) valueType);
                int array_len = ((AST_NEW_EXP_IDX)new_exp).size;  // TODO-EX4 use it in the future - insert it as a property ot the symbol table
            } else {
                // regular assignment check
                valid = TYPE.checkAssignment(semantic_type, valueType);
            }

            if (!valid) {
                this.throw_error("Assignment for variable declaration failed");
            }
        }
        /***************************************************/
        /* [2] Enter the Function Type to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(name, semantic_type);

    }

    public TYPE SemantMe() throws SemanticException {
        SemantMe_checkAssignment(this.getType());
        return null;  // Return value is irrelevant for declarations
    }

    public TYPE getType() throws SemanticException {
        // getType() isn't changing the Symbol Table & not checking var assignment
        if (this.varType == null)
            this.varType = SemantMe_declaredType();
        return this.varType;
    }
    public String getName(){
        return this.name;
    }

}