package AST;

import EXCEPTIONS.SemanticException;
import TYPES.*;
import TEMP.*;

public class AST_STMT_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_STMT head;
	public AST_STMT_LIST next;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_LIST(AST_STMT head, AST_STMT_LIST next, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/

		if (next != null) System.out.print("====================== stmts -> stmt stmts\n");
		if (next == null) System.out.print("====================== stmts -> stmt      \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
		this.next = next;
		this.lineNumber = lineNumber;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE STMT LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.PrintMe();
		if (next != null) next.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nLIST\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
		if (next != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, next.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException {
		// semant all the list recursively:
		if (head != null) head.SemantMe();
		if (next != null) next.SemantMe();

		return null; // there is no TYPE for sequence of statements.
	}

	public TEMP IRme()
	{
		if (head != null) head.IRme();
		if (next != null) next.IRme();

		return null;
	}
}
