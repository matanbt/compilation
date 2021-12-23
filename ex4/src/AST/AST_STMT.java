package AST;

import TEMP.TEMP;
import TYPES.*;

public abstract class AST_STMT extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}
	public TYPE SemantMe()
	{
		return null;
	}

	public TEMP IRme() {
		System.out.println("[DEBUG] IRme called inside AST_STMT. Did you forget to override me?");
		return null;
	}

}
