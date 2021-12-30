/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public class MIPSGenerator
{


	/*******************************/
	/* Constants for our usage ... */
	/******************************/
	/* How many temporary we backup and restore inside a function? */
	public static final int TEMP_TO_BACKUP_COUNT = 10;

	public static String LABEL_STRING_ACCESS_VIOLATION = "Label_string_access_violation";
	public static String LABEL_STRING_ILLEGAL_DIV_BY_0 = "Label_string_illegal_div_by_zero";
	public static String LABEL_STRING_INVALID_PTR_DREF = "Label_string_invalid_ptr_dref";



	private int WORD_SIZE=4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		// TODO .text ?
		fileWriter.print(".text\n");
		/* 1. Invokes user_main, i.e. the main() function of the L program */
		fileWriter.print("main:\n");
		fileWriter.print("\tjal user_main\n");

		/* 2. Performs exit */
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}
	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");  // space char
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}
	public void print_string(TEMP t)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,4\n");
		fileWriter.format("\tsyscall\n");
	}
	//public TEMP addressLocalVar(int serialLocalVarNum)
	//{
	//	TEMP t  = TEMP_FACTORY.getInstance().getFreshTEMP();
	//	int idx = t.getSerialNumber();
	//
	//	fileWriter.format("\taddi Temp_%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
	//	
	//	return t;
	//}
	public void allocate(String var_name)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\t%s: .word 0\n",var_name);
	}

	public void allocateString(String var_name, String val)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\t%s: .asciiz \"%s\"\n", var_name, val);
		fileWriter.format(".text\n");
	}
  
	public void loadString(TEMP dst, String str_name)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tla Temp_%d, %s\n",idxdst, str_name);
	}

	public void allocateWithIntVal(String name, int val)
	{
		// name = val
		// assumes name is unique
		fileWriter.format(".data\n");
		fileWriter.format("\t%s: .word %d\n",name, val);
	}
	public void allocateByReferenceName(String name, String referenceName)
	{
		// name = referenceName
		// assumes name is unique
		fileWriter.format(".data\n");
		fileWriter.format("\t%s: .word %s\n",name, referenceName);
	}

	/* Loads variable from .data segment by given name */
	public void loadGlobal(TEMP dst, String var_name)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,global_%s\n", idxdst, var_name);
	}

	/* Loads variable from the stack by given offset (relative to $fp) */
	public void loadFromStack(TEMP dst, int offset) {
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,%d(fp)\n", idxdst, offset);
	}

  public void load_by_var_name(TEMP dst,String full_var_name)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,%s\n",idxdst,full_var_name);
	}

	public void storeGlobal(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,var_name);		
	}

	public void storeToStack(int offset,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,%d(fp)\n",idxsrc, offset);
	}

	public void li(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
	}
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tsub Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void div(TEMP dst,TEMP oprnd_left,TEMP oprnd_right)
	{
		// dst = floor(oprnd_left / oprnd_right)
		int i_l = oprnd_left.getSerialNumber();
		int i_r = oprnd_right.getSerialNumber();
		int dstidx = dst.getSerialNumber();

		fileWriter.format("\tdiv Temp_%d,Temp_%d\n", i_l, i_r);  // quotient stored in special register- lo
		fileWriter.format("\tmflo Temp_%d\n", dstidx);  // move from lo
	}
	public void label(String inlabel)
	{
		fileWriter.format("%s:\n",inlabel);
	}	
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}	
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);				
	}


	/* private methods */

	private void exit_due_to_runtime_check(String msg_name)
	{
		/* msg_name = "string_illegal_div_by_0" or "string_access_violation" or "string_invalid_ptr_dref" */
		TEMP temp_print_msg = TEMP_FACTORY.getInstance().getFreshTEMP();
		load_by_var_name(temp_print_msg, msg_name);
		this.print_string(temp_print_msg);
		this.finalizeFile();
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
				String filename=String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");

			/* global labels */
			instance.fileWriter.format(".text\n");

			instance.label(LABEL_STRING_ACCESS_VIOLATION);
			instance.exit_due_to_runtime_check("string_access_violation");

			instance.label(LABEL_STRING_ILLEGAL_DIV_BY_0);
			instance.exit_due_to_runtime_check("string_illegal_div_by_0");

			instance.label(LABEL_STRING_INVALID_PTR_DREF);
			instance.exit_due_to_runtime_check("string_invalid_ptr_dref");
		}
		return instance;
	}
}
