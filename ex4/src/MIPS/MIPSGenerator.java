/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;


public class MIPSGenerator {


    /*******************************/
    /* Constants for our usage ... */
    /******************************/
    /* How many temporary we backup and restore inside a function? */
    public static final int TEMP_TO_BACKUP_COUNT = 10;

    public static String LABEL_STRING_ACCESS_VIOLATION = "Label_string_access_violation";
    public static String LABEL_STRING_ILLEGAL_DIV_BY_0 = "Label_string_illegal_div_by_zero";
    public static String LABEL_STRING_INVALID_PTR_DREF = "Label_string_invalid_ptr_dref";


    private int WORD_SIZE = 4;
    private int CHAR_SIZE = 1;


    public int getCharSizeInBytes() {
        return this.CHAR_SIZE;
    }

    /***********************/
    /* The file writer ... */
    /***********************/
    private PrintWriter fileWriter;

    /* ---------------------- Allocations ---------------------- */

    public void allocate(String var_name) {
        fileWriter.format(".data\n");
        fileWriter.format("\t%s: .word 0\n", var_name);
        fileWriter.format(".text\n");
    }

    public void allocateString(String var_name, String val) {
        fileWriter.format(".data\n");
        fileWriter.format("\t%s: .asciiz \"%s\"\n", var_name, val);
        fileWriter.format(".text\n");
    }

    public void allocateWithIntVal(String name, int val) {
        // name = val
        // assumes name is unique
        fileWriter.format(".data\n");
        fileWriter.format("\t%s: .word %d\n", name, val);
        fileWriter.format(".text\n");
    }

    public void allocateByReferenceName(String name, String referenceName) {
        // name = referenceName
        // assumes name is unique
        fileWriter.format(".data\n");
        fileWriter.format("\t%s: .word %s\n", name, referenceName);
        fileWriter.format(".text\n");
    }

    public void allocateWordsArray(String name, List<String> referenceNamesList) {
        // the words will be stored in consecutive memory location
        // name = address of first word allocated
        // assumes name is unique
        fileWriter.format(".data\n");
        fileWriter.format("\t%s:\n", name);
        for (int i = 0; i < referenceNamesList.size(); i++) {
            fileWriter.format("\t\t.word %s\n", referenceNamesList.get(i));
        }
        fileWriter.format(".text\n");
    }

    public void malloc(TEMP dst, TEMP len) {
        /* dst = malloc(len)
        allocates 'len' amount of bytes, allocation address stored in 'dst' */
        fileWriter.format("\tli $v0, 9\n");
        fileWriter.format("\tmove $a0, Temp_%d\n", len.getSerialNumber());
        fileWriter.format("\tsyscall\n");
        fileWriter.format("\tmove Temp_%d, $v0\n", dst.getSerialNumber());
    }

  public void mallocWords(TEMP dst, TEMP len) {
        // dst = malloc(len * WORD_SIZE)
        fileWriter.format("\tli $v0, 9\n");
        fileWriter.format("\tmove $a0, Temp_%d\n", len.getSerialNumber());
        fileWriter.format("\tmul $a0, $a0, %d\n", WORD_SIZE);
        fileWriter.format("\tsyscall\n");
        fileWriter.format("\tmove Temp_%d, $v0\n", dst.getSerialNumber());
    }

    /* ---------------------- Load & Stores ---------------------- */

    /* Loads variable from .data segment by given name */
    public void loadGlobal(TEMP dst, String var_name) {
        String full_var_name = String.format("global_%s", var_name);
        this.loadByVarName(dst, full_var_name);
    }

    public void storeGlobal(String var_name, TEMP src) {
        int idxsrc = src.getSerialNumber();
        fileWriter.format("\tsw Temp_%d,global_%s\n", idxsrc, var_name);
    }

    public void loadByVarName(TEMP dst, String full_var_name) {
        int idxdst = dst.getSerialNumber();
        fileWriter.format("\tlw Temp_%d,%s\n", idxdst, full_var_name);
    }

    /* Loads variable from the stack by given offset (relative to $fp) */
    public void loadFromStack(TEMP dst, int offset_in_bytes) {
        int idxdst = dst.getSerialNumber();
        fileWriter.format("\tlw Temp_%d,%d($fp)\n", idxdst, offset_in_bytes);
    }

    /* Stores variable in the stack by given offset (relative to $fp) */
    public void storeToStack(int offset_in_bytes, TEMP src) {
        int idxsrc = src.getSerialNumber();
        fileWriter.format("\tsw Temp_%d,%d($fp)\n", idxsrc, offset_in_bytes);
    }

    public void loadFromHeap(TEMP dst, TEMP base_address, int offset_in_bytes) {
        int idxdst = dst.getSerialNumber();
        int idxBaseAddress = base_address.getSerialNumber();
        fileWriter.format("\tlw Temp_%d,%d(Temp_%d)\n", idxdst, offset_in_bytes, idxBaseAddress);
    }

    public void loadFromHeap(TEMP dst, TEMP base_address, TEMP offset_in_bytes) {
        fileWriter.format("\tadd $s0, Temp_%d, Temp_%d\n", base_address.getSerialNumber(), offset_in_bytes.getSerialNumber());
        fileWriter.format("\tlw Temp_%d, 0($s0)\n", dst.getSerialNumber());
    }

    public void loadByteFromHeap(TEMP dst, TEMP base_address, int offset_in_bytes) {
        int idxdst = dst.getSerialNumber();
        fileWriter.format("\tlb Temp_%d, %d(Temp_%d)\n", idxdst, offset_in_bytes, base_address.getSerialNumber());
    }

    public void storeToHeap(TEMP src, TEMP base_address, int offset_in_bytes) {
        int idxsrc = src.getSerialNumber();
        fileWriter.format("\tsw Temp_%d, %d(Temp_%d)\n", idxsrc, offset_in_bytes, base_address.getSerialNumber());
    }

    public void storeByteToHeap(TEMP src, TEMP base_address, int offset_in_bytes) {
        int idxsrc = src.getSerialNumber();
        int idxBaseAddress = base_address.getSerialNumber();
        fileWriter.format("\tsw Temp_%d,%d(Temp_%d)\n", idxsrc, offset, idxBaseAddress);
        fileWriter.format("\tsb Temp_%d, %d(Temp_%d)\n", idxsrc, offset_in_bytes, base_address.getSerialNumber());
    }

    public void storeToHeap(TEMP src, TEMP base_address, TEMP offset_in_bytes) {
        fileWriter.format("\tadd $s0, Temp_%d, Temp_%d\n", base_address.getSerialNumber(), offset_in_bytes.getSerialNumber());
        fileWriter.format("\tsw Temp_%d, 0($s0)\n", src.getSerialNumber());
    }

    public void loadAddressByName(TEMP dst, String name) {
        int idxdst = dst.getSerialNumber();
        fileWriter.format("\tla Temp_%d, %s\n", idxdst, name);
    }
  
  /* -------------- Stack access functions -------------- */

    // TODO in the following functions I assume we're writing to `.text` session, that is -
    //  every function that "opens" an `.data` section is responsible to close it as well.

    /* Pushing a register (e.g. "$v0", "$fp", "$t5"...) to the "top" of the stack */
    public void pushRegisterToStack(String register) {
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw %s,0($sp)\n", register);
    }

    /* Pops the "top" of the stack and loads it to the given register (e.g. "$v0") */
    public void popToRegisterFromStack(String register) {
        fileWriter.format("\tlw %s,0($sp)\n", register);
        fileWriter.format("\taddu $sp, $sp, 4\n");
    }


    /* ---------------------- Operators and Arithmetic ---------------------- */

    public void add(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
        int dstidx = dst.getSerialNumber();

        fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n", dstidx, i1, i2);
    }

    public void addi(TEMP dst, TEMP temp_oprnd, int int_oprnd) {
        // dst = temp_oprnd + int_oprnd
        int oprnd_idx = temp_oprnd.getSerialNumber();
        int dstidx = dst.getSerialNumber();

        fileWriter.format("\taddi Temp_%d, Temp_%d, %d\n", dstidx, oprnd_idx, int_oprnd);
    }

    public void sub(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
        int dstidx = dst.getSerialNumber();

        fileWriter.format("\tsub Temp_%d,Temp_%d,Temp_%d\n", dstidx, i1, i2);
    }

    public void mul(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
        int dstidx = dst.getSerialNumber();

        fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n", dstidx, i1, i2);
    }

    public void div(TEMP dst, TEMP oprnd_left, TEMP oprnd_right) {
        // dst = floor(oprnd_left / oprnd_right)
        int i_l = oprnd_left.getSerialNumber();
        int i_r = oprnd_right.getSerialNumber();
        int dstidx = dst.getSerialNumber();

        fileWriter.format("\tdiv Temp_%d,Temp_%d\n", i_l, i_r);  // quotient stored in special register- lo
        fileWriter.format("\tmflo Temp_%d\n", dstidx);  // move from lo
    }


    /* ---------------------- Jump (Branching) ---------------------- */

    public void label(String inlabel) {
        fileWriter.format("%s:\n", inlabel);
    }

    public void jump(String inlabel) {
        fileWriter.format("\tj %s\n", inlabel);
    }


    /* ---------------------- Conditional Jump (Branching) ---------------------- */

    public void blt(TEMP oprnd1, TEMP oprnd2, String label) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();

        fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n", i1, i2, label);
    }

    public void bge(TEMP oprnd1, TEMP oprnd2, String label) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();

        fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n", i1, i2, label);
    }

    public void bne(TEMP oprnd1, TEMP oprnd2, String label) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();

        fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n", i1, i2, label);
    }

    public void beq(TEMP oprnd1, TEMP oprnd2, String label) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();

        fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n", i1, i2, label);
    }

    public void beqz(TEMP oprnd1, String label) {
        int i1 = oprnd1.getSerialNumber();

        fileWriter.format("\tbeq Temp_%d,$zero,%s\n", i1, label);
    }

    public void bltz(TEMP oprnd1, String label) {
        int i1 = oprnd1.getSerialNumber();
        fileWriter.format("\tbltz Temp_%d, %s\n", i1, label);
    }


	/* -------------- (Callee) Functions related MIPS code -------------- */
	/* Sets to return value ($v0) to be the value in `t` */
	public void setReturn(TEMP t) {
		int idx = t.getSerialNumber();
		fileWriter.format("\tmove $v0,Temp_%d\n", idx);
	}

	public void functionPrologue(int localsCount) {
		/* 1. Push important registers to stack */
		pushRegisterToStack("$ra");
		pushRegisterToStack("$fp");

		/* 2. Set the new frame-pointer */
		fileWriter.format("\tmove $fp, $sp");

		/* 3. Backup all temp-registers by pushing them to stack as well */
		for (int i = 0; i < TEMP_TO_BACKUP_COUNT; i++) {
			pushRegisterToStack(String.format("$t%d", i));
		}

		/* 4. Keep some room for local variables in the stack */
		fileWriter.format("\tsubu $sp, $sp, %d", localsCount * WORD_SIZE);
	}

	public void functionEpilogue(int localsCount) {
		/* 1. Removes local from the stack */
		fileWriter.format("\taddu $sp, $sp, %d", localsCount * WORD_SIZE);

		/* 2. Restores all temp-registers by popping them from the stack */
		for (int i = TEMP_TO_BACKUP_COUNT - 1; i >= 0; i--) {
			popToRegisterFromStack(String.format("$t%d", i));
		}

		/* 3. Restores important registers from stack */
		popToRegisterFromStack("$fp");
		popToRegisterFromStack("$ra");

		/* 4. Jump back */
        jumpToReturnAddress();
	}

    public void jumpToReturnAddress() {
        fileWriter.format("\tjr $ra\n");
    }


    /* -------------- (Caller) Functions related MIPS code -------------- */

    /* Prologue code before the caller invokes the function / method */
    public void functionCallerPrologue(List<TEMP> argValues) {
        /* Pushes the arguments to the stack in reverse order */
        for (int i = argValues.size() - 1; i >= 0; i--) {
            String argValueTemp = String.format("$Temp_%d", argValues.get(i).getSerialNumber());
            pushRegisterToStack(argValueTemp);
        }
    }

    /* perform jal to the given (full) label */
    public void functionJumpAndLink(String full_label) {
        fileWriter.format("\tjal %s\n", full_label);
    }

    /* fetches the method-address from vTable and `jalr` to its */
    public void methodJumpAndLink(TEMP classObject, int methodOffset) {
        /* load the vTable pointer to $s0 */
        fileWriter.format("\tlw $s0 0(TEMP_%d)\n", classObject.getSerialNumber());

        /* load the method pointer to $s1 */
        fileWriter.format("\tlw $s1 %d($s0)\n", methodOffset);

        /* jump (and link) to the method */
        fileWriter.format("\tjalr $s1\n");
    }

    public void functionCallerEpilogue(int argsCount, TEMP dstRtn) {
        /* Removes all arguments that were located CallerPrologue */
        fileWriter.format("\taddu $sp, $sp, %d", argsCount * WORD_SIZE);

        if (dstRtn != null) {
            /* Saves the return value to 'dstRtn' */
            fileWriter.format("\tmove TEMP_%d, $v0", dstRtn.getSerialNumber());
        }
    }


    /* ---------------------- Built-in functions ---------------------- */

    public void print_int(TEMP t) {
        int idx = t.getSerialNumber();

        /* Prints the integer in 't' */
        fileWriter.format("\tmove $a0,Temp_%d\n", idx);
        fileWriter.format("\tli $v0,1\n");
        fileWriter.format("\tsyscall\n");

        /* Prints space char (as required by exercise's instructions) */
        fileWriter.format("\tli $a0,32\n");  // space char
        fileWriter.format("\tli $v0,11\n");
        fileWriter.format("\tsyscall\n");
    }

    public void print_string(TEMP t) {
        int idx = t.getSerialNumber();

        /* Prints the string in pointed by 't' */
        fileWriter.format("\tmove $a0,Temp_%d\n", idx);
        fileWriter.format("\tli $v0,4\n");
        fileWriter.format("\tsyscall\n");
    }

    /* ---------------------- Other ---------------------- */

    public void li(TEMP t, int value) {
        int idx = t.getSerialNumber();
        fileWriter.format("\tli Temp_%d,%d\n", idx, value);
    }

    public void move(TEMP dst, TEMP src) {
        fileWriter.format("\tmove Temp_%d, Temp_%d\n", dst.getSerialNumber(), src.getSerialNumber());
    }

    /* ---------------------- Beginning and Ending of the MIPS code ---------------------- */

    /* USUAL SINGLETON IMPLEMENTATION ... */
    private static MIPSGenerator instance = null;

    /* PREVENT INSTANTIATION ... */
    protected MIPSGenerator() {}

    /* Beginning */
    public static MIPSGenerator getInstance() {
        if (instance == null) {
            /*******************************/
            /* [0] The instance itself ... */
            /*******************************/
            instance = new MIPSGenerator();

            try {
                /*********************************************************************************/
                /* [1] Open the MIPS text file and write data section with error message strings */
                /*********************************************************************************/
                String dirname = "./output/";
                String filename = String.format("MIPS.txt");

                /***************************************/
                /* [2] Open MIPS text file for writing */
                /***************************************/
                instance.fileWriter = new PrintWriter(dirname + filename);
            } catch (Exception e) {
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

    /* End resulted by error */
    private void exit_due_to_runtime_check(String msg_name) {
        /* msg_name = "string_illegal_div_by_0" or "string_access_violation" or "string_invalid_ptr_dref" */
        TEMP temp_print_msg = new SAVED(0);
        loadAddressByName(temp_print_msg, msg_name);
        this.print_string(temp_print_msg);
        this.performExit();
    }

    /* exits the program */
    private void performExit() {
        fileWriter.print("\tli $v0,10\n");
        fileWriter.print("\tsyscall\n");
    }

    /* End */
    public void finalizeFile() {

        /* 1. Invokes user_main, i.e. the main() function of the L program */
        fileWriter.print("main:\n");
        fileWriter.print("\tjal user_main\n");

        /* 2. Performs exit */
        performExit();

        /* 3. Closes file */
        fileWriter.close();
    }
}
