package AST;

import EXCEPTIONS.SemanticException;
import IR.IDVariable;
import IR.IRcommand_Array_set;
import IR.IRcommand_Field_set;
import IR.IRcommand_Store;
import TEMP.TEMP;
import TYPES.TYPE;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var, AST_EXP exp, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
		this.lineNumber = lineNumber;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException {
		TYPE leftType, rightType;

		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		leftType = this.var.SemantMe();
		rightType = this.exp.SemantMe();

		if (leftType == null || rightType == null)
		{
			if (leftType == null)
			{
				this.throw_error("Trying to assign to void");
			}
			else
			{
				this.throw_error("Trying to assign void to variable");
			}
		}


		/***************************************************/
		/* [3] Check for the given value is from expected type */
		/***************************************************/
		boolean valid = TYPE.checkAssignment(leftType, rightType);
		if(!valid) {
			this.throw_error("Variable assignment statement");
		}

		/*********************************************************/
		/* [5] Return value is irrelevant for statements */
		/*********************************************************/
		return null;
	}

	public TEMP IRme()
	{
		/*
		 * TODO: we should think how we think of AST_VARs IRme, as these ASTs are used both as right-values and left-values,
		 *       here, of course, I treat this.var as left-value (i.e. a pointer).
		 *
		 * TODO: The solution I suggest for handling left-values:
		 *       Add inner members to each AST_VAR that will be fields (e.g. arrPointer inside AST_VAR_SUBSCRIPT)
		 *       and we'll use them when assigning to a var. (e.g. it will prepare the parameters of IRcommand_Array_set)
		 *       IRme will return the *right-value* of the variable (e.g. it will invoke IRcommand_Array_access)
		 */

		/* We assign differently to each AST_VAR class */
		TEMP src = exp.IRme();

		/* The statement is of sort: var := (y + 2) */
		if (var instanceof AST_VAR_SIMPLE) {
			// We do not need to invoke var.IRme, as there is no need to its temp
			IDVariable dst = ((AST_VAR_SIMPLE) var).idVar;
			IR.getInstance().Add_IRcommand(new IRcommand_Store(dst, src));
		}

		/* The statement is of sort: obj.otherObj.member := (y + 2) */
		else if (var instanceof AST_VAR_FIELD) {
			TEMP objectPointer = ((AST_VAR_FIELD) var).getObjectPointer();
			String fieldName = ((AST_VAR_FIELD) var).fieldName;
			IR.getInstance().Add_IRcommand(new IRcommand_Field_set(objectPointer, fieldName, src));
		}

		/* The statement is of sort: arrPointer[subscriptIndex] := src */
		else if (var instanceof AST_VAR_SUBSCRIPT) {
			TEMP arrPointer = ((AST_VAR_SUBSCRIPT) var).getArrayPointer();
			TEMP subscriptIndex = ((AST_VAR_SUBSCRIPT) var).getSubscriptIndex();
			IR.getInstance().Add_IRcommand(new IRcommand_Array_set(arrPointer, subscriptIndex, src));
		}

		/* Shouldn't get here */
		else {
			System.out.print("[DEBUG] Warning: unexpected var in AST_STMT_ASSIGN.IRme()\n");
		}

		return null;
	}
}
