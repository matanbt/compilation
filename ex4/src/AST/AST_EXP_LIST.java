package AST;

import EXCEPTIONS.SemanticException;
import TYPES.TYPE;

public class AST_EXP_LIST extends AST_Node
{
	public AST_EXP head; // could NOT be null
	public AST_EXP_LIST next; // could be null (means end of the list)


	public AST_EXP_LIST(AST_EXP head, AST_EXP_LIST next, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (next != null) System.out.print("====================== exp_list -> exp exp_list\n");
		if (next == null) System.out.print("====================== exp_list -> exp      \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
		this.next = next;
		this.lineNumber = lineNumber;
	}


	public void PrintMe()
	{
		System.out.print("AST NODE EXP-LIST\n");

		if (head != null) head.PrintMe();
		if (next != null) next.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\nLIST\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
		if (next != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, next.SerialNumber);
	}

	@Override
	public TYPE SemantMe() throws SemanticException {
		// this is actually never being called (we only use EXP_LIST in functionCallSemantMe), so we return null
		return null;
	}

	public TEMP IRme()
	{
		// this is never being called
		return null;
	}

}
