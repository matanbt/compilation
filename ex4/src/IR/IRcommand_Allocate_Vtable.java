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

import AST.AST_DEC_FUNC;
import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TEMP.SAVED;
import TYPES.TYPE_CLASS;

import java.util.List;

public class IRcommand_Allocate_Vtable extends IRcommand
{
	TYPE_CLASS type_class;
	String class_name;
	List<AST_DEC_FUNC> methods_list;
	String vt_name, label_vt_init;

	public IRcommand_Allocate_Vtable(TYPE_CLASS type_class)
	{
		this.type_class = type_class;
		this.class_name = type_class.name;
		this.methods_list = type_class.methods_list;
		this.vt_name = String.format("vt_%s", class_name);
		this.label_vt_init = String.format("Label_vt_init_%s", class_name);
		type_class.vt_name = this.vt_name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator mips = MIPSGenerator.getInstance();
		/* allocate space for the vtable */
		mips.allocateWordsArray(vt_name, methods_list.size());

		/* vtable init - will be called before user's main (see mips.finalizeFile())
		 * stores the methods' addresses in the vt */
		mips.vt_init_labels.add(label_vt_init);

		TEMP address_of_vt = new SAVED(0);
		TEMP method_address = new SAVED(1);

		mips.label(label_vt_init);

		mips.loadAddressByName(address_of_vt, vt_name);

		for (int i = 0; i < methods_list.size(); i++) {
			AST_DEC_FUNC method_dec = methods_list.get(i);
			mips.loadAddressByName(method_address, method_dec.funcStartingLabel);
			mips.storeToHeap(method_address, address_of_vt, type_class.getMethodOffset(method_dec.funcName));
		}
		mips.jumpToReturnAddress();
	}
}
