package AST;

import EXCEPTIONS.SemanticException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_FOR_SCOPE_BOUNDARIES;
import TYPES.TYPE_INT_INSTANCE;
import TEMP.*;
import IR.*;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond, AST_STMT_LIST body, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> WHILE ( cond ) { body } \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.cond = cond;
		this.body = body;
		this.lineNumber = cond.lineNumber;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE WHILE STMT\n");

		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"WHILE");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
	}


	public TYPE SemantMe() throws SemanticException {
		/****************************/
		/* [0] Semant the Condition + verifications*/
		/****************************/
		if (cond.SemantMe() != TYPE_INT_INSTANCE.getInstance())
		{
			this.throw_error("condition inside WHILE is not int");
		}

		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope(TYPE_FOR_SCOPE_BOUNDARIES.IF_WHILE_SCOPE, this);

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		body.SemantMe();

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/*********************************************************/
		/* [4] Return value is irrelevant for WHILE statements */
		/*********************************************************/
		return null;
	}


	public TEMP IRme()
	{
		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end   = IRcommand.getFreshLabel("WHILE_end");
		String label_start = IRcommand.getFreshLabel("WHILE_start");
		IRcommand start_ir = new IRcommand_Label(label_start);
		IRcommand end_ir = new IRcommand_Label(label_end);

		/*********************************/
		/* [2] entry label for the while */
		/*********************************/
		IR.
		getInstance().
		Add_IRcommand(start_ir);

		/********************/
		/* [3] cond.IRme(); */
		/********************/
		TEMP cond_temp = cond.IRme();

		/******************************************/
		/* [4] Jump conditionally to the loop end */
		/******************************************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,label_end, end_ir));

		/*******************/
		/* [5] body.IRme() */
		/*******************/
		body.IRme();

		/******************************/
		/* [6] Jump to the loop entry */
		/******************************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Jump_Label(label_start, start_ir));

		/**********************/
		/* [7] Loop end label */
		/**********************/
		IR.
		getInstance().
		Add_IRcommand(end_ir);

		/*******************/
		/* [8] return null */
		/*******************/
		return null;
	}
}