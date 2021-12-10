package AST;
import EXCEPTIONS.SemanticException;
import TYPES.*;

public class AST_EXP_BINOP extends AST_EXP
{
	public String op;
	public AST_EXP left;
	public AST_EXP right;

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
		sOP = this.op; // TODO if we'll change the type of "op", we should support conversion to string here

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

				if (!(class_e1.isSubClassOf(class_e2) || class_e2.isSubClassOf(class_e1)))
				{
					this.throw_error(String.format("Trying to check for equality for 2 not related classes %s %s", class_e1, class_e2));
				}
			}
			else if (e1.canBeAssignedNil() && e2 != TYPE_NIL_INSTANCE.getInstance())
			{
				this.throw_error(String.format(message, e1, e2));
			}
			else if (e2.canBeAssignedNil() && e1 != TYPE_NIL_INSTANCE.getInstance())
			{
				this.throw_error(String.format(message, e1, e2));
			}
		}
	}

	public TYPE SemantMe() throws SemanticException
	{
		TYPE semantic_left = null;
		TYPE semantic_right = null;

		if (left  != null) semantic_left = left.SemantMe();
		if (right != null) semantic_right = right.SemantMe();

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
		if ((semantic_left == TYPE_STRING.getInstance()) && (semantic_right == TYPE_STRING.getInstance()) && (op.equals("+")))
		{
			return TYPE_STRING_INSTANCE.getInstance();
		}
		this.throw_error(String.format("binary operations between invalid/unmatching types: left = (%s), right = (%s)", semantic_left.name, semantic_right.name));
		return null;
	}

}
