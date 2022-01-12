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

import AST.*;
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
		TEMP vt_address = new SAVED(1);
		TEMP field_val = new SAVED(2);

		/* Allocate memory for the new object */
		mips.li(size_of_instance, class_of_instance.fields_list.size() + 1); // +1 for the vtable
		mips.mallocWords(dst, size_of_instance);

		/* set first word to be the address of the vtable */
		mips.loadAddressByName(vt_address, class_of_instance.vt_name);
		mips.storeToHeap(vt_address, dst, 0);

		/* store the fields' initial values */
		/* assumption: a declared data member inside a class can be initialized only as constant integer, string or nil */
		for (int i = 0; i < class_of_instance.fields_list.size(); i++) {
			AST_DEC_VAR ast_field_dec = class_of_instance.fields_list.get(i);
			AST_EXP ast_field_val = ast_field_dec.exp;
			// ast_field_val instanceof AST_EXP_INT or AST_EXP_STRING or AST_EXP_NIL (can be null - field declared without initialization)

			/* set field_val with this field's value */

			if (ast_field_val == null || ast_field_val instanceof AST_EXP_NIL) {
				mips.li(field_val, 0);
			}

			else if (ast_field_val instanceof AST_EXP_INT) {
				mips.li(field_val, ((AST_EXP_INT)ast_field_val).value);
			}

			else if (ast_field_val instanceof AST_EXP_STRING) {
				/* This string is initialized in this field's declaration (in the class declaration),
				and its memory location is saved by the label defined in TYPE_CLASS - getStringFieldGlobalName() */
				mips.loadAddressByName(field_val, class_of_instance.getStringFieldGlobalName(ast_field_dec.name));
			}

			else {
				System.out.println("[DEBUG] Warning: unexpected AST_EXP in IRcommand_New_Class_Instance_Init");
			}

			/* store the field's value */
			mips.storeToHeap(field_val, dst, class_of_instance.getFieldOffset(ast_field_dec.name));
		}
	}
}
