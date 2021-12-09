package AST;

import EXCEPTIONS.SemanticException;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_CLASS_INSTANCE;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_FIELD(AST_VAR var,String fieldName, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
		this.lineNumber = lineNumber;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();
		System.out.format("FIELD NAME( %s )\n",fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n...->%s",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	@Override
	public TYPE SemantMe() throws SemanticException {
		// finds the type of the variable (possibly recursive, and the base case is AST_VAR_SIMPLE.SemantMe)
		TYPE var_type = this.var.SemantMe();

		if (var_type == null) {
			// Shouldn't get here
			this.throw_error(">> ERROR got bad type from variable");
		}

		// var_type must be a class-instance if it wants to access a field
		if(!var_type.isInstanceOfSomeClass()) {
			this.throw_error(String.format(">> ERROR type (%s) is not class-instance and has no fields", var_type.name));
		}

		// finds the field in this class or in its supers'
		TYPE field_type = ((TYPE_CLASS)((TYPE_CLASS_INSTANCE) var_type).getSymbolType()).findInClassAndSuperClasses(this.fieldName);

		if(field_type == null) {
			this.throw_error(String.format(">> ERROR failed to resolve field (%s) from variable (%s)", this.fieldName , var_type.name));
		}

		// a variable must be assignable
		// We looked for CField, so it's possible we got back a method or maybe another unexpected type
		if (!field_type.isInstanceOfType()) {
			this.throw_error(String.format(">> ERROR failed to resolve field (%s) from variable (%s)", this.fieldName , var_type.name));
		}

		return field_type;
	}
}
