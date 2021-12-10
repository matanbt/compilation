package AST;

import EXCEPTIONS.SemanticException;
import TYPES.*;

public abstract class AST_DEC extends AST_Node
{
	// -------------------- Semantic Additions - relevant for function and vars declaration --------------------
	// non-null means declaration in 'encompassingClass', and null means it's not in a class scope
	public TYPE_CLASS encompassingClass = null;

	public TYPE getType() throws SemanticException {
		return null;
	}

	public String getName(){
		return null;
	}

	// ---------------------------------------------------------------------------------------------------------

	public TYPE SemantMe() throws SemanticException {
		return null;
	}

}
