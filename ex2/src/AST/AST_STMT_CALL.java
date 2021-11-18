package AST;

public class AST_STMT_CALL extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR v; // name of the function to be called
	public String idName; // name of the function to be called
	public AST_EXP_LIST eList; // name of the function to be called

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_CALL(AST_VAR v, String idName, AST_EXP_LIST eList)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.print("====================== stmt -> "+idName+"(); \n");

		this.v = v;
		this.idName = idName;
		this.eList = eList;
	}

	// overloading constructors
	public AST_STMT_CALL(String idName)
	{
		this(null, idName, null);
	}

	public AST_STMT_CALL(String idName, AST_EXP_LIST eList)
	{
		this(null, idName, eList);
	}

	public AST_STMT_CALL(AST_VAR v, String idName)
	{
		this(v, idName, null);
	}


	public void PrintMe()
	{
		System.out.print("AST NODE CALL "+idName+" STMT\n");

		if (v != null) v.PrintMe();
		if (eList != null) eList.PrintMe();


		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CALL("+ idName +")\n");

		if (v != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, v.SerialNumber);
		if (eList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, eList.SerialNumber);
	}
}
