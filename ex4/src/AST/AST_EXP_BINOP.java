package AST;
import EXCEPTIONS.SemanticException;
import TEMP.TEMP;
import TEMP.TEMP_FACTORY;
import TYPES.*;
import IR.*;

public class AST_EXP_BINOP extends AST_EXP
{
	public String op;
	public AST_EXP left;
	public AST_EXP right;
	private TYPE type_of_expressions = null;  // TYPE of left,right. annotation for IRme, initiated in SemantMe()

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,String op, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.op = op;
		this.lineNumber = lineNumber;
	}

	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		String sOP="";

		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		sOP = this.op;

		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%s)",sOP));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}

	private void checkEqualityTesting(TYPE e1, TYPE e2) throws SemanticException
	{
		if (e1 != e2)
		{
			String message = "Mismatched types: %s %s";
			if ((e1 instanceof TYPE_CLASS_INSTANCE) && (e2 instanceof TYPE_CLASS_INSTANCE))
			{
				TYPE_CLASS class_e1 = (TYPE_CLASS) ((TYPE_CLASS_INSTANCE)e1).getSymbolType();
				TYPE_CLASS class_e2 = (TYPE_CLASS) ((TYPE_CLASS_INSTANCE)e2).getSymbolType();

				if (class_e1.isSubClassOf(class_e2) || class_e2.isSubClassOf(class_e1))
				{
					return;
				}
			}
			else if (e1.canBeAssignedNil() && e2 == TYPE_NIL_INSTANCE.getInstance())
			{
				return;
			}
			else if (e2.canBeAssignedNil() && e1 == TYPE_NIL_INSTANCE.getInstance())
			{
				return;
			}

			this.throw_error(String.format(message, e1, e2));
		}
	}

	public TYPE SemantMe() throws SemanticException
	{
		TYPE semantic_left = null;
		TYPE semantic_right = null;

		if (left  != null) semantic_left = left.SemantMe();
		if (right != null) semantic_right = right.SemantMe();

		this.type_of_expressions = semantic_left;  // annotation for IRme, can be null

		/* Equality testing */
		if (op.equals("="))
		{
			this.checkEqualityTesting(semantic_left, semantic_right);
			return TYPE_INT_INSTANCE.getInstance();
		}

		if ((semantic_left == TYPE_INT_INSTANCE.getInstance()) && (semantic_right == TYPE_INT_INSTANCE.getInstance()))
		{
			if (op.equals("/") && (right instanceof AST_EXP_INT) && (((AST_EXP_INT) right).value == 0)) {
				this.throw_error("explicit zero division");
			}
			return TYPE_INT_INSTANCE.getInstance();
		}
		if ((semantic_left == TYPE_STRING_INSTANCE.getInstance()) && (semantic_right == TYPE_STRING_INSTANCE.getInstance()) && (op.equals("+")))
		{
			return TYPE_STRING_INSTANCE.getInstance();
		}
		this.throw_error("binary operations between invalid/unmatching types");
		return null;
	}


	public TEMP IRme()
	{
		TEMP left_t = null;
		TEMP right_t = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR ir = IR.getInstance();

		if (left  != null) left_t = left.IRme();
		if (right != null) right_t = right.IRme();

		if (type_of_expressions == TYPE_STRING_INSTANCE.getInstance()) {
			if (op.equals("+")) {
				ir.Add_IRcommand(new IRcommand_Binop_Add_Strings(dst, left_t, right_t));
			}
			else if (op.equals("=")) {
				ir.Add_IRcommand(new IRcommand_Binop_EQ_Strings(dst, left_t, right_t));  // TODO IRcommand_Binop_EQ_Strings
			}
		}

		else {
			if (op.equals("=")) {
				ir.Add_IRcommand(new IRcommand_Binop_EQ(dst, left_t, right_t));
			}

			// else --> type_of_expressions == TYPE_INT_INSTANCE.getInstance()

			else if (op.equals("<"))
			{
				ir.Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,left_t,right_t));
			}

			else if (op.equals(">"))
			{
				ir.Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,right_t,left_t));
			}

			else{
				// op can be: "+", "-", "*", "/"
				ir.Add_IRcommand(new IRcommand_Binop_Arithmetic(dst,left_t,right_t, op));
			}
		}

		return dst;
	}
}
