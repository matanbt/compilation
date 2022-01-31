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
import IR.IRcommand;
import TEMP.*;


public class MIPSGenerator {

    public static String outputFilename = null;  // path to output generated mips code

    /*******************************/
    /* Constants for our usage ... */
    /******************************/
    /* How many temporary we backup and restore inside a function? */
    public static final int TEMP_TO_BACKUP_COUNT = 10;

    public static String LABEL_STRING_ACCESS_VIOLATION = "Label_string_access_violation";
    public static String LABEL_STRING_ILLEGAL_DIV_BY_0 = "Label_string_illegal_div_by_zero";
    public static String LABEL_STRING_INVALID_PTR_DREF = "Label_string_invalid_ptr_dref";


    public final int WORD_SIZE = 4;
    public final int CHAR_SIZE = 1;


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
        fileWriter.format("\n.text\n");
    }

    public void allocateString(String var_name, String val) {
        fileWriter.format(".data\n");
        fileWriter.format("\t%s: .asciiz \"%s\"\n", var_name, val);
        fileWriter.format("\n.text\n");
    }

    public void allocateWithIntVal(String name, int val) {
        // name = val
        // assumes name is unique
        fileWriter.format(".data\n");
        fileWriter.format("\t%s: .word %d\n", name, val);
        fileWriter.format("\n.text\n");
    }

    public void allocateByReferenceName(String name, String referenceName) {
        // name = referenceName
        // assumes name is unique
        fileWriter.format(".data\n");
        fileWriter.format("\t%s: .word %s\n", name, referenceName);
        fileWriter.format("\n.text\n");
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
        fileWriter.format("\n.text\n");
    }

    public void malloc(TEMP dst, TEMP len) {
        /* dst = malloc(len)
        allocates 'len' amount of bytes, allocation address stored in 'dst' */
        fileWriter.format("\tli $v0, 9\n");
        fileWriter.format("\tmove $a0, %s\n", len.getRegisterName());
        fileWriter.format("\tsyscall\n");
        fileWriter.format("\tmove %s, $v0\n", dst.getRegisterName());
    }

  public void mallocWordsWithNullInit(TEMP dst, TEMP len) {
        // (!) NOTICE: overrides $s6, $s7
        // dst = malloc(len * WORD_SIZE)
        fileWriter.format("\tli $v0, 9\n");
        fileWriter.format("\tmove $a0, %s\n", len.getRegisterName());
        fileWriter.format("\tmul $a0, $a0, %d\n", WORD_SIZE);
        fileWriter.format("\tsyscall\n");
        fileWriter.format("\tmove %s, $v0\n", dst.getRegisterName());

        // init all words cells to null (0)
        String curr_word_address = "$s6";
        String next_addr_after_end = "$s7";  // the next address after the end of the new allocated word sequence
        String loop_lbl = IRcommand.getFreshLabel("loop_mallocWordsWithNullInit");
        String end_lbl = IRcommand.getFreshLabel("end_mallocWordsWithNullInit");

        fileWriter.format("\tmove %s, %s\n", curr_word_address, dst.getRegisterName());
        fileWriter.format("\tadd %s, $a0, %s\n", next_addr_after_end, dst.getRegisterName());

        label(loop_lbl);
        fileWriter.format("\tbeq %s, %s, %s\n", curr_word_address, next_addr_after_end, end_lbl);
        fileWriter.format("\tsw $zero, 0(%s)\n", curr_word_address);
        fileWriter.format("\taddi %s, %s, %d\n", curr_word_address, curr_word_address, WORD_SIZE);
        jump(loop_lbl);

        label(end_lbl);
    }

    /* ---------------------- Load & Stores ---------------------- */

    public void storeByVarName(String var_name, TEMP src) {
        fileWriter.format("\tsw %s, %s\n", src.getRegisterName(), var_name);
    }

    public void loadByVarName(TEMP dst, String full_var_name) {
        fileWriter.format("\tlw %s, %s\n", dst.getRegisterName(), full_var_name);
    }

    /* Loads variable from the stack by given offset (relative to $fp) */
    public void loadFromStack(TEMP dst, int offset_in_bytes) {
        fileWriter.format("\tlw %s, %d($fp)\n", dst.getRegisterName(), offset_in_bytes);
    }

    /* Stores variable in the stack by given offset (relative to $fp) */
    public void storeToStack(int offset_in_bytes, TEMP src) {
        fileWriter.format("\tsw %s, %d($fp)\n", src.getRegisterName(), offset_in_bytes);
    }

    public void loadFromHeap(TEMP dst, TEMP base_address, int offset_in_bytes) {
        fileWriter.format("\tlw %s, %d(%s)\n", dst.getRegisterName(), offset_in_bytes, base_address.getRegisterName());
    }

    public void loadFromHeap(TEMP dst, TEMP base_address, TEMP offset_in_bytes) {
        fileWriter.format("\tadd $s0, %s, %s\n", base_address.getRegisterName(), offset_in_bytes.getRegisterName());
        fileWriter.format("\tlw %s, 0($s0)\n", dst.getRegisterName());
    }

    public void loadByteFromHeap(TEMP dst, TEMP base_address, int offset_in_bytes) {
        fileWriter.format("\tlb %s, %d(%s)\n", dst.getRegisterName(), offset_in_bytes, base_address.getRegisterName());
    }

    public void storeToHeap(TEMP src, TEMP base_address, int offset_in_bytes) {
        fileWriter.format("\tsw %s, %d(%s)\n", src.getRegisterName(), offset_in_bytes, base_address.getRegisterName());
    }

    public void storeByteToHeap(TEMP src, TEMP base_address, int offset_in_bytes) {
        String src_reg_name = src.getRegisterName();
        fileWriter.format("\tsb %s, %d(%s)\n", src_reg_name, offset_in_bytes, base_address.getRegisterName());
    }

    public void storeToHeap(TEMP src, TEMP base_address, TEMP offset_in_bytes) {
        fileWriter.format("\tadd $s0, %s, %s\n", base_address.getRegisterName(), offset_in_bytes.getRegisterName());
        fileWriter.format("\tsw %s, 0($s0)\n", src.getRegisterName());
    }

    public void loadAddressByName(TEMP dst, String name) {
        fileWriter.format("\tla %s, %s\n", dst.getRegisterName(), name);
    }
  
  /* -------------- Stack access functions -------------- */

    // TODO in the following functions I assume we're writing to `.text` session, that is -
    //  every function that "opens" an `.data` section is responsible to close it as well.

    /* Pushing a register (e.g. "$v0", "$fp", "$t5"...) to the "top" of the stack */
    public void pushRegisterToStack(String register) {
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw %s, 0($sp)\n", register);
    }

    /* Pops the "top" of the stack and loads it to the given register (e.g. "$v0") */
    public void popToRegisterFromStack(String register) {
        fileWriter.format("\tlw %s, 0($sp)\n", register);
        fileWriter.format("\taddu $sp, $sp, 4\n");
    }


    /* ---------------------- Operators and Arithmetic ---------------------- */

    public void add(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        fileWriter.format("\tadd %s, %s, %s\n", dst.getRegisterName(), oprnd1.getRegisterName(), oprnd2.getRegisterName());
    }

    public void addi(TEMP dst, TEMP temp_oprnd, int int_oprnd) {
        // dst = temp_oprnd + int_oprnd
        fileWriter.format("\taddi %s, %s, %d\n", dst.getRegisterName(), temp_oprnd.getRegisterName(), int_oprnd);
    }

    public void sub(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        fileWriter.format("\tsub %s, %s, %s\n", dst.getRegisterName(), oprnd1.getRegisterName(), oprnd2.getRegisterName());
    }

    public void mul(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        fileWriter.format("\tmul %s, %s, %s\n", dst.getRegisterName(), oprnd1.getRegisterName(), oprnd2.getRegisterName());
    }

    public void div(TEMP dst, TEMP oprnd_left, TEMP oprnd_right) {
        // dst = floor(oprnd_left / oprnd_right)
        fileWriter.format("\tdiv %s, %s\n", oprnd_left.getRegisterName(), oprnd_right.getRegisterName());  // quotient stored in special register- lo
        fileWriter.format("\tmflo %s\n", dst.getRegisterName());  // move from lo
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
        fileWriter.format("\tblt %s, %s, %s\n", oprnd1.getRegisterName(), oprnd2.getRegisterName(), label);
    }

    public void bge(TEMP oprnd1, TEMP oprnd2, String label) {
        fileWriter.format("\tbge %s, %s, %s\n", oprnd1.getRegisterName(), oprnd2.getRegisterName(), label);
    }

    public void bne(TEMP oprnd1, TEMP oprnd2, String label) {
        fileWriter.format("\tbne %s, %s, %s\n", oprnd1.getRegisterName(), oprnd2.getRegisterName(), label);
    }

    public void beq(TEMP oprnd1, TEMP oprnd2, String label) {
        fileWriter.format("\tbeq %s, %s, %s\n", oprnd1.getRegisterName(), oprnd2.getRegisterName(), label);
    }

    public void beqz(TEMP oprnd1, String label) {
        fileWriter.format("\tbeq %s, $zero, %s\n", oprnd1.getRegisterName(), label);
    }

    public void bltz(TEMP oprnd1, String label) {
        fileWriter.format("\tbltz %s, %s\n", oprnd1.getRegisterName(), label);
    }


	/* -------------- (Callee) Functions related MIPS code -------------- */
	/* Sets to return value ($v0) to be the value in `t` */
	public void setReturn(TEMP t) {
		fileWriter.format("\tmove $v0, %s\n", t.getRegisterName());
	}

	public void functionPrologue(int localsCount) {
		/* 1. Push important registers to stack */
		pushRegisterToStack("$ra");
		pushRegisterToStack("$fp");

		/* 2. Set the new frame-pointer */
		fileWriter.format("\tmove $fp, $sp\n");

		/* 3. Backup all temp-registers by pushing them to stack as well */
		for (int i = 0; i < TEMP_TO_BACKUP_COUNT; i++) {
			pushRegisterToStack(String.format("$t%d", i));
		}

		/* 4. Keep some room for local variables in the stack */
		fileWriter.format("\tsubu $sp, $sp, %d\n", localsCount * WORD_SIZE);
	}

	public void functionEpilogue(int localsCount) {
		/* 1. Removes local from the stack */
		fileWriter.format("\taddu $sp, $sp, %d\n", localsCount * WORD_SIZE);

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
        fileWriter.format("\tjr $ra\n\n\n");
    }


    /* -------------- (Caller) Functions related MIPS code -------------- */

    /* Prologue code before the caller invokes the function / method */
    public void functionCallerPrologue(List<TEMP> argValues) {
        /* Pushes the arguments to the stack in reverse order */
        for (int i = argValues.size() - 1; i >= 0; i--) {
            String argValueTemp = String.format("%s", argValues.get(i).getRegisterName());
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
        fileWriter.format("\tlw $s0, 0(%s)\n", classObject.getRegisterName());

        /* load the method pointer to $s1 */
        fileWriter.format("\tlw $s1, %d($s0)\n", methodOffset);

        /* jump (and link) to the method */
        fileWriter.format("\tjalr $s1\n");
    }

    public void functionCallerEpilogue(int argsCount, TEMP dstRtn) {
        /* Removes all arguments that were located CallerPrologue */
        fileWriter.format("\taddu $sp, $sp, %d\n", argsCount * WORD_SIZE);

        if (dstRtn != null) {
            /* Saves the return value to 'dstRtn' */
            fileWriter.format("\tmove %s, $v0\n", dstRtn.getRegisterName());
        }
    }


    /* ---------------------- Built-in functions ---------------------- */

    public void print_int(TEMP t) {
        /* Prints the integer in 't' */
        fileWriter.format("\tmove $a0, %s\n", t.getRegisterName());
        fileWriter.format("\tli $v0, 1\n");
        fileWriter.format("\tsyscall\n");

        /* Prints space char (as required by exercise's instructions) */
        fileWriter.format("\tli $a0, 32\n");  // space char
        fileWriter.format("\tli $v0, 11\n");
        fileWriter.format("\tsyscall\n");
    }

    public void print_string(TEMP t) {
        /* Prints the string in pointed by 't' */
        fileWriter.format("\tmove $a0, %s\n", t.getRegisterName());
        fileWriter.format("\tli $v0, 4\n");
        fileWriter.format("\tsyscall\n");
    }

    /* ---------------------- Other ---------------------- */

    public void li(TEMP t, int value) {
        fileWriter.format("\tli %s, %d\n", t.getRegisterName(), value);
    }

    public void move(TEMP dst, TEMP src) {
        fileWriter.format("\tmove %s, %s\n", dst.getRegisterName(), src.getRegisterName());
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
                /***************************************/
                /* [1] Open MIPS text file for writing */
                /***************************************/
                instance.fileWriter = new PrintWriter(MIPSGenerator.outputFilename);

            } catch (Exception e) {
                e.printStackTrace();
            }

            /*****************************************************/
            /* [3] Print data section with error message strings */
            /*****************************************************/
            instance.fileWriter.print(".data\n");
            instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
            instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Division By Zero\"\n");
            instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");

            /* global labels */
            instance.fileWriter.format("\n.text\n");

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
        fileWriter.print("\tli $v0, 10\n");
        fileWriter.print("\tsyscall\n\n\n");
    }

    /* End */
    public void finalizeFile() {

        /* 1. Invokes user_main, i.e. the main() function of the L program */
        fileWriter.print("main:\n");
        fileWriter.print("\tjal func_user_main\n");

        /* 2. Performs exit */
        performExit();

        /* 3. Closes file */
        fileWriter.close();
    }
}
