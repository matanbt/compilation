package AST;

import TEMP.TEMP;

public abstract class AST_VAR extends AST_Node
{
    public TEMP IRme() {
        System.out.println("[DEBUG] IRme called inside AST_VAR. Did you forget to override me?");
        return null;
    }

    public abstract void IRmeAsLeftValue(AST_EXP src);

}
