package AST;

import EXCEPTIONS.SemanticException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_CFIELD extends AST_Node
{
	/********************************/
	/*  CField ::= funcDec | varDec */
	/********************************/
	public AST_DEC dec;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_CFIELD(AST_DEC dec, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== cField -> Dec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		// dec MUST BE instance of "limited" DEC_VAR or DEC_FUNC. This is enforced by the grammar (cup file).
		this.dec = dec;
		this.lineNumber = lineNumber;
	}

	/*********************************************************/
	/* The printing message for an CFIELD FUNC AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST CFIELD FUNC */
		/********************************************/
		System.out.print("AST NODE CFIELD\n");

		/***********************************/
		/* RECURSIVELY PRINT FUNC */
		/***********************************/
		if (dec != null) dec.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,dec.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException {
		SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();

		/* Check that this CField name is unique or overriding super class
		(overriding means that it has the same signature/type)
		DO this BEFORE:
		(1) Adding the field to the Symbol Table (so findInClassScope(), findInClassAndSuperClasses() won't find this new field)
		(2) Entering the function scope / checking the var assignment (to ensure that in the case of an ERROR we will output the right line) */
		TYPE_CLASS encompassingClass = table.findScopeClass();
		this.dec.encompassingClass = encompassingClass;
		String field_name = this.dec.getName();

		/* Verify uniqueness in the class scope */
		if (table.findInCurrentScope(field_name) != null){  // current scope has to be the class scope in this point
			this.throw_error(String.format("identifier (%s) used in two declaration in the same class scope",
					field_name));
		}

		TYPE field_type = this.dec.getType();  // this isn't doing (1), (2)

		/* Verify there is no method overloading / variable shadowing */
		TYPE pre_defined_type = encompassingClass.findInClassAndSuperClasses(field_name);  // won't be null only if found in the supers classes,
		// (TODO ?): could run findInSuperClasses() instead
		if (pre_defined_type != null) {
			/* Verify new declaration is overriding the previous one (has the same signature/type)
			 * - If both are variables, their TYPE is the same java object
			 * - if both are functions, TYPE_FUNCTION.equals() is called */
			if (field_type != pre_defined_type) {
				this.throw_error(String.format("class identifier (%s) overshadowing the super class " +
						"declaration", field_name));
			}
		}

		/* Add cfield to the class's cfield_list
		* Each semanted AST_CFIELD is added immediately to cfield_list (so it can be used inside the cfield scope (if
		* it's a function) and be used by the next cfields) */
		CFIELD new_cfield = new CFIELD(field_type, field_name);
		CFIELD_LIST cfield_list = encompassingClass.cfield_list;
		if (cfield_list == null){
			encompassingClass.cfield_list = new CFIELD_LIST(new_cfield, null);
		}
		else{
			cfield_list.addLast(new_cfield);
		}

		/* Finish SemantMe */
		this.dec.SemantMe();

		return null;
	}
}
