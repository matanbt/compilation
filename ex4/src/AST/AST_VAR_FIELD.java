package AST;

import EXCEPTIONS.SemanticException;
import TEMP.TEMP;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_CLASS_INSTANCE;
import TEMP.TEMP_FACTORY;
import IR.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;

	// annotation for IR
	private IDVariable idField;  // initiate in this.SemantMe()

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
		if(! (var_type instanceof TYPE_CLASS_INSTANCE)) {
			this.throw_error(String.format("type (%s) is not class-instance and has no fields",
					var_type.name));
		}
		TYPE_CLASS type_class = (TYPE_CLASS)(var_type).convertInstanceToSymbol();

		this.idField = new IDVariable(this.fieldName, VarRole.CFIELD_VAR, type_class);

		// finds the field in this class or in its supers'
		TYPE field_type = type_class.findInClassAndSuperClasses(this.fieldName);

		if(field_type == null) {
			this.throw_error(String.format("failed to resolve field (%s) from variable (%s)",
					this.fieldName , var_type.name));
		}

		// a variable must be assignable
		// We looked for CField, so it's possible we got back a method or maybe another unexpected type
		if (!field_type.isInstanceOfType()) {
			this.throw_error(String.format("failed to resolve field (%s) from variable (%s)",
					this.fieldName , var_type.name));
		}

		return field_type;
	}

	/* returns the right-value of the field */
	public TEMP IRme() {
		TEMP objectPointer = this.var.IRme();
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

		mIR.Add_IRcommand(new IRcommand_Field_access(dst, objectPointer, this.idField));

		return dst;
	}

	/* performs a set on the object field (treating it as a left-value)  */
	/* The statement is of sort: obj.otherObj.member := (y + 2) */
	public void IRmeAsLeftValue(AST_EXP src) {
		TEMP objectPointer = this.var.IRme();
		TEMP src_temp = src.IRme();
		mIR.Add_IRcommand(new IRcommand_Field_set(objectPointer, this.idField, src_temp));
	}
}
