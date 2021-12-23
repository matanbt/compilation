package AST;

import EXCEPTIONS.SemanticException;
import TEMP.TEMP;
import TYPES.*;

public abstract class AST_EXP extends AST_Node
{
	public TYPE SemantMe() throws SemanticException
	{
		return null;
	}

	public TEMP IRme()
	{
		System.out.println("[DEBUG] IRme called inside AST_EXP. Did you forget to override me?");
		return null;
	}
}