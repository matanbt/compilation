/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

import AST.AST_EXP;
import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TEMP.SAVED;
import TYPES.TYPE_CLASS;

public class IRcommand_New_Class_Instance_Init extends IRcommand
{
	TEMP dst;  // its value will be a the address of the new instance
	TYPE_CLASS class_of_instance;

	public IRcommand_New_Class_Instance_Init(TEMP dst, TYPE_CLASS type_class)
	{
		this.dst = dst;
		this.class_of_instance = type_class;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator mips = MIPSGenerator.getInstance();

		TEMP size_of_instance = new SAVED(0);  // size in words
		TEMP vt_address = new SAVED(2);

		/* Allocate memory for the new object */
		mips.li(size_of_instance, class_of_instance.fields_list.size() + 1); // +1 for the vtable
		MIPSGenerator.getInstance().mallocWords(dst, size_of_instance);

		/* set first word to be the address of the vtable */
		mips.loadAddressByName(vt_address, class_of_instance.vt_name);
		mips.storeToHeap(vt_address, dst, 0);

		/* store the fields' initial values */
		/* assumption: a declared data member inside a class can be initialized only as constant integer, string or nil */
		for (int offset = 1; offset <= class_of_instance.fields_list.size(); offset++) {
			AST_EXP ast_field_val = class_of_instance.fields_list.get(offset - 1).exp;
			// ast_field_val instanceof AST_EXP_INT or AST_EXP_STRING or AST_EXP_NIL (can be null)
			mips.storeToHeap(ast_field_val.IRme(), dst, offset);
		}
	}
}
