package AST;

public class AST_CLASSDEC extends AST_Node
{
	public AST_CFIELD_LIST lst;
	public String superClassName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CLASSDEC(AST_CFIELD_LIST lst,String superClassName)
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
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.lst = lst;
		this.superClassName = superClassName;
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
		if (lst != null) lst.PrintMe();
		if (superClassName != null) System.out.format("SUPER CLASS NAME( %s )\n",superClassName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (superClassName != null)
            AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("CLASSDEC\nextends %s",superClassName));
        else
            AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("CLASSDEC"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (lst != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,lst.SerialNumber);
	}
}
