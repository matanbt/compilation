package AST;
import TYPES.TYPE;
import TYPES.TYPE_INT;
import TYPES.TYPE_STRING;

public class AST_EXP_BINOP extends AST_EXP
{
	public String op;
	public AST_EXP left;
	public AST_EXP right;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,String op)
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
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.op = op;
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
	public TYPE SemantMe()
	{
		TYPE t1 = null;
		TYPE t2 = null;

		if (left  != null) t1 = left.SemantMe();
		if (right != null) t2 = right.SemantMe();

		if ((t1 == TYPE_INT.getInstance()) && (t2 == TYPE_INT.getInstance()))
		{
			if (op.equals("/") && (right instanceof AST_EXP_INT) && (((AST_EXP_INT) right).value == 0)){  // zero division
				System.exit(0);  // TODO- error handling
				return null;
			}
			return TYPE_INT.getInstance();
		}
		if ((t1 == TYPE_STRING.getInstance()) && (t2 == TYPE_STRING.getInstance()) && (op.equals("+")))
		{
			return TYPE_STRING.getInstance();
		}
		System.exit(0);  // TODO- error handling
		return null;
	}

}
