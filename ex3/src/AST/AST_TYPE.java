package AST;

public class AST_TYPE extends AST_Node
{
	public String type_name; // type can be from: {'void', 'int', 'string'}, OR could be a cutomized type
	

	public AST_TYPE(String type_name, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== TYPE(%s) \n", type_name);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type_name = type_name;

		this.lineNumber = lineNumber;
	}


	public void PrintMe()
	{
		System.out.format("AST NODE TYPE(%s) \n", this.type_name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("TYPE(%s)", this.type_name));
	}
}
