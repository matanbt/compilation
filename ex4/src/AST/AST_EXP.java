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
		return null;
	}
}