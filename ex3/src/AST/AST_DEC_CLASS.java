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
		if (superClassName != null){
			father = table.find(this.superClassName);
			if (! (father instanceof TYPE_CLASS)){
				this.throw_error("class declaration- father class name is not a class");
			}
		}

		/* Create the declared class's TYPE_CLASS */
		CFIELD_LIST cfield_list = new CFIELD_LIST(null, null);
		CFIELD_LIST cfield_list_tail = cfield_list;
		TYPE_CLASS declared_class_type = new TYPE_CLASS((TYPE_CLASS) father, this.className, cfield_list);

		/* Enter the Class Type to the Symbol Table */
		table.enter(this.className, declared_class_type);

		/* Begin Class Scope */
		table.beginScope(TYPE_FOR_SCOPE_BOUNDARIES.CLASS_SCOPE, this, declared_class_type);

		/* Semant the class's AST_CFIELD_LIST
		* Each AST_CFIELD that being semanted is added immediately to cfield_list (so it can be used by the next cfields) */
		for (AST_CFIELD_LIST it = this.cfield_lst; it  != null; it = it.next)
		{
			AST_CFIELD ast_cfield = it.head;
			ast_cfield.SemantMe();  // --> field is added to the Symbol Table (last element there)

			String cfield_name = table.getLastEntryName();
			TYPE cfield_type = table.getLastEntryType();

			// add to cfield_list
			if (cfield_list_tail.head == null){  // if cfield_list is empty
				cfield_list_tail.head = new CFIELD(cfield_type, cfield_name);
			}
			else{
				cfield_list_tail.next = new CFIELD_LIST(new CFIELD(cfield_type, cfield_name), null);
				cfield_list_tail = cfield_list_tail.next;
			}
		}

		/* Check if there are no cfields in the class*/
		if (cfield_list.head == null){
			cfield_list = null;
		}

		/* End Scope */
		table.endScope();

		/* Return value is irrelevant for class declarations */
		return null;
	}
}
