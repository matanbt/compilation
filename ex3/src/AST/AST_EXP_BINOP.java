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
		TYPE semantic_left = null;
		TYPE semantic_right = null;

		if (left  != null) semantic_left = left.SemantMe();
		if (right != null) semantic_right = right.SemantMe();

		if ((semantic_left == TYPE_INT.getInstance()) && (semantic_right == TYPE_INT.getInstance()))
		{
			if (op.equals("/") && (right instanceof AST_EXP_INT) && (((AST_EXP_INT) right).value == 0)){
				System.out.println(">> ERROR explicit zero division");
				System.exit(0);  // TODO- error handling
				return null;
			}
			return TYPE_INT.getInstance();
		}
		if ((semantic_left == TYPE_STRING.getInstance()) && (semantic_right == TYPE_STRING.getInstance()) && (op.equals("+")))
		{
			return TYPE_STRING.getInstance();
		}
		System.out.format(">> ERROR binary operations between invalid/unmatching types: left = (%s), right = (%s)\n", semantic_left.name, semantic_right.name);
		System.exit(0);  // TODO- error handling
		return null;
	}

}
