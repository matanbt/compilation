package AST;

import EXCEPTIONS.SemanticException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.TYPE;
import TYPES.TYPE_ARRAY_INSTANCE;

public class AST_STMT_ASSIGN_NEW extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_NEW_EXP new_exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN_NEW(AST_VAR var, AST_NEW_EXP new_exp, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN newExp SEMICOLON  \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.new_exp = new_exp;
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
		if (new_exp != null) new_exp.PrintMe();

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
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,new_exp.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException {
		TYPE leftType, rightType;

		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		leftType = this.var.SemantMe();
		rightType = this.new_exp.SemantMe();
		if (leftType == null || rightType == null)
		{ // shouldn't be here, should return an error way before
			this.throw_error("failed when typing var-assign-statement (SHOULDN'T GET HERE)");
		}



		/***************************************************/
		/* [3] Check for the given value is from expected type */
		/***************************************************/
		boolean valid = true;
		if (rightType instanceof TYPE_ARRAY_INSTANCE) {
			// Special case of creating a new array instance
			valid = TYPE.checkNewArrayAssignment(leftType, (TYPE_ARRAY_INSTANCE) rightType);
		} else {
			// must be case of creating a class instance
			valid = TYPE.checkAssignment(leftType, rightType);
		}

		if(!valid) {
			this.throw_error("Variable Assignment NEW Statement");
		}

		/*********************************************************/
		/* [5] Return value is irrelevant for statements */
		/*********************************************************/
		return null;
	}

	public TEMP IRme()
	{
		var.IRmeAsLeftValue(new_exp);
		return null;
	}
}
