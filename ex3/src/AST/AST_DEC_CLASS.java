package AST;

import EXCEPTIONS.SemanticException;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_CLASS extends AST_DEC
{
	public AST_CFIELD_LIST cfield_lst;
	public String className;
	public String superClassName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_CLASS(AST_CFIELD_LIST cfield_lst, String className, String superClassName, int lineNumber)
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
		this.cfield_lst = cfield_lst;
		this.className = className;
		this.superClassName = superClassName;
		this.lineNumber = lineNumber;
	}


	public AST_DEC_CLASS(AST_CFIELD_LIST cfield_lst, String className, int lineNumber)
	{
		this(cfield_lst, className, null, lineNumber);
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
		if (cfield_lst != null) cfield_lst.PrintMe();
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
		if (cfield_lst != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cfield_lst.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException {
		// We are in the global scope (forced syntactically)

		SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();

		/* Check that the class name is valid (AKA not used) */
		if (table.find(this.className) != null)
		{
			this.throw_error("class declaration- class name already used");
		}

		/* Get father class */
		TYPE father = null;
		if (superClassName != null) {
			father = table.find(this.superClassName);
			if (father == null)
				this.throw_error("class declaration- father class name is not defined");
			if (! (father instanceof TYPE_CLASS)){
				this.throw_error("class declaration- father class name is not a class");
			}
		}

		/* Create the declared class's TYPE_CLASS */
		TYPE_CLASS declared_class_type = new TYPE_CLASS((TYPE_CLASS) father, this.className, null);

		/* Enter the Class Type to the Symbol Table */
		table.enter(this.className, declared_class_type);

		/* Begin Class Scope */
		table.beginScope(TYPE_FOR_SCOPE_BOUNDARIES.CLASS_SCOPE, this, declared_class_type);

		/* Semant the class's AST_CFIELD_LIST */
		this.cfield_lst.SemantMe();

		/* End Scope */
		table.endScope();

		/* Return value is irrelevant for class declarations */
		return null;
	}
}
