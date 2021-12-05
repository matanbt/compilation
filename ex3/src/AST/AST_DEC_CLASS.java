package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_CLASS extends AST_DEC
{
	public AST_CFIELD_LIST lst;
	public String className;
	public String superClassName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_CLASS(AST_CFIELD_LIST lst, String className, String superClassName, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (superClassName != null)
		    System.out.format("====================== classDec -> CLASS ID EXTENDS %s {cField [ cField ]*}\n",superClassName);
		else
		    System.out.format("====================== classDec -> CLASS ID {cField [ cField ]*}\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.lst = lst;
		this.className = className;
		this.superClassName = superClassName;
		this.lineNumber = lineNumber;
	}

	/*************************************************/
	/* The printing message for a class dec AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST CLASSDEC */
		/*********************************/
		System.out.print("AST NODE CLASSDEC\n");

		/**********************************************/
		/* RECURSIVELY PRINT AST_CFIELD_LIST, then SUPER CLASS NAME ... */
		/**********************************************/
		if (lst != null) lst.PrintMe();
		if (superClassName != null) System.out.format("SUPER CLASS NAME( %s )\n",superClassName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (superClassName != null)
            AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("DEC CLASS(%s)\nextends(%s)",className, superClassName));
        else
            AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("DEC CLASS(%s)", className));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (lst != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,lst.SerialNumber);
	}

	public TYPE SemantMe()
	{
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope();

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		TYPE_CLASS t = new TYPE_CLASS(null, className, lst.SemantMe());

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		SYMBOL_TABLE.getInstance().enter(className,t);

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}
}
