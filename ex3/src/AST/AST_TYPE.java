package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;

public class AST_TYPE extends AST_Node
{
	public String type_name; // type can be from: {'void', 'int', 'string'}, OR could be a cutomized type


	public AST_TYPE(String type_name)
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
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type_name = type_name;
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

	public TYPE SemantMe() {
		// gets the type by identifier
		return SYMBOL_TABLE.getInstance().find(type_name);
	}
}
