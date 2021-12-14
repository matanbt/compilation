package AST;

import TYPES.TYPE;

public class AST_DEC_CLASS_SIG extends AST_Node {
    public String className;
    public String superClassName;

    public AST_DEC_CLASS_SIG(String className, String superClassName, int lineNumber)
    {
        SerialNumber = AST_Node_Serial_Number.getFresh();

        if (superClassName != null)
            System.out.format("====================== classDec -> CLASS ID EXTENDS %s {cField [ cField ]*}\n",superClassName);
        else
            System.out.format("====================== classDec -> CLASS ID {cField [ cField ]*}\n");

        this.className = className;
        this.superClassName = superClassName;
        this.lineNumber = lineNumber;
    }

    public AST_DEC_CLASS_SIG(String className, int lineNumber)
    {
        this(className, null, lineNumber);
    }

    public void PrintMe()
    {
        /* Class node will print this signature, so we won't do anything here */
    }

    public TYPE SemantMe() {
        return null;
    }
}
