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
import TYPES.TYPE_CLASS;

import java.util.ArrayList;
import java.util.List;

public class IRcommand_Allocate_Vtable extends IRcommand
{
	List<AST_DEC_FUNC> methods_list;
	String vt_name;

	public IRcommand_Allocate_Vtable(TYPE_CLASS type_class)
	{
		this.methods_list = type_class.methods_list;
		this.vt_name = String.format("vt_%s", type_class.name);
		type_class.vt_name = this.vt_name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		/* vtable initialization */
		List<String> methodsStartingLabels = new ArrayList<>();
		for (int i = 0; i < methods_list.size(); i++) {
			methodsStartingLabels.add(methods_list.get(i).funcStartingLabel);
		}
		MIPSGenerator.getInstance().allocateWordsArray(vt_name, methodsStartingLabels);
	}
}
