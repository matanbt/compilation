package AST;

import EXCEPTIONS.SemanticException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_VOID;

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

	/*
		Returns the SymbolType of this node, or null if it represents 'void'
		Meaning null could be a good result for AST_DEC_FUNC

		Invariant: every non-null returned here isSymbolType() == true.
	 */
	public TYPE SemantMe() throws SemanticException {
		// we have the identifier 'type_name' and it claims to be a legit type
		TYPE purposed_type = SYMBOL_TABLE.getInstance().find(type_name);

		if (purposed_type == null) {
			this.throw_error(String.format("Couldn't find identifier with name (%s)", type_name));
		}
		if(purposed_type == TYPE_VOID.getInstance()) {
			return null; // we'll refer to 'void' as result null of SemantMe
		}
		if(!purposed_type.isSymbolType()) {
			// validate that the symbol is actually a type-symbol (like 'int')
			this.throw_error(String.format("Symbol (%s) is not a valid type!" +
					"(this might be caused due to overshadowing of its identifier)", type_name));
		}
		assert !purposed_type.name.equals(type_name); // sanity check for us
		return purposed_type; // success
	}

}
