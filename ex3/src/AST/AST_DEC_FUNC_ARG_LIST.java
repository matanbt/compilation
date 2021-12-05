package AST;

import TYPES.TYPE;

public class AST_DEC_FUNC_ARG_LIST extends AST_Node
{
	public AST_DEC_FUNC_ARG head;      // could NOT be null
	public AST_DEC_FUNC_ARG_LIST next; // could be null (means end of the list)


	public AST_DEC_FUNC_ARG_LIST(AST_DEC_FUNC_ARG head, AST_DEC_FUNC_ARG_LIST next)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (next != null) System.out.print("====================== func_args -> func_arg func_args\n");
		if (next == null) System.out.print("====================== func_args -> func_arg      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.next = next;
	}


	public void PrintMe()
	{
		System.out.print("AST NODE FUNC-ARGS LIST\n");

		if (head != null) head.PrintMe();
		if (next != null) next.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"FUNC-ARGS\nLIST\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
		if (next != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, next.SerialNumber);
	}

	public TYPE SemantMe() {
		// no TYPE for argument-list, we do analyze it in AST_DEC_FUNC
		System.out.print("[DEBUG] Warning: unexpected call of AST_DEC_FUNC_ARG_LIST.SemantMe()\n");
		return null;
	}
}
