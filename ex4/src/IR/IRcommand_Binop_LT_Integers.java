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
import TEMP.*;
import MIPS.*;

import java.util.HashSet;

public class IRcommand_Binop_LT_Integers extends IRcommand
{
	// dst = t1 < t2
	public TEMP t1;
	public TEMP t2;
	public TEMP dst;

	public IRcommand_Binop_LT_Integers(TEMP dst,TEMP t1,TEMP t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}

	public void updateInSet()
	{
		this.in_set = new HashSet<TEMP>(this.out_set);
		this.in_set.remove(dst);
		this.in_set.add(t1);
		this.in_set.add(t2);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end        = getFreshLabel("BINOP_LT_end");
		String label_AssignOne  = getFreshLabel("BINOP_LT_AssignOne");
		String label_AssignZero = getFreshLabel("BINOP_LT_AssignZero");
		
		/******************************************/
		/* [2] if (t1< t2) goto label_AssignOne;  */
		/*     if (t1>=t2) goto label_AssignZero; */
		/******************************************/
		MIPSGenerator.getInstance().blt(t1,t2,label_AssignOne);
		MIPSGenerator.getInstance().bge(t1,t2,label_AssignZero);

		/************************/
		/* [3] label_AssignOne: */
		/*                      */
		/*         t3 := 1      */
		/*         goto end;    */
		/*                      */
		/************************/
		MIPSGenerator.getInstance().label(label_AssignOne);
		MIPSGenerator.getInstance().li(dst,1);
		MIPSGenerator.getInstance().jump(label_end);

		/*************************/
		/* [4] label_AssignZero: */
		/*                       */
		/*         t3 := 1       */
		/*         goto end;     */
		/*                       */
		/*************************/
		MIPSGenerator.getInstance().label(label_AssignZero);
		MIPSGenerator.getInstance().li(dst,0);
		MIPSGenerator.getInstance().jump(label_end);

		/******************/
		/* [5] label_end: */
		/******************/
		MIPSGenerator.getInstance().label(label_end);
	}
}
