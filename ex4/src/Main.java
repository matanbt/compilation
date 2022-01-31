   
import java.io.*;
import java.io.PrintWriter;

import EXCEPTIONS.SemanticException;
import java_cup.runtime.Symbol;
import AST.*;
import IR.*;
import MIPS.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AST_DEC_LIST AST;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);
			
			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l, file_writer);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			AST = (AST_DEC_LIST) p.parse().value;
			
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			AST.PrintMe();

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			// TODO: make sure the following code was migrated properly from ex3,
			//       as we have new output requirements.
			try
			{
				AST.SemantMe();

				/* Verifies there exist a main global-function */
				if(!AST_DEC_FUNC.isFoundMain) {
					AST.throw_error("L-program must contain a 'void main()` global function");
				}
				
				file_writer.println("OK");
			}
			catch (SemanticException e)
			{
				System.out.println(e.getMessage());
				file_writer.println(String.format("ERROR(%d)", e.lineNumber));

				// We throw here another error, to ensure the compilation does not continue
				file_writer.close();
				AST.throw_error("Aborting Compilation Phase: Got semantic error during compilation");
			}

			/**********************/
			/* [8] IR the AST ... */
			/**********************/
			AST.IRme();

			/****************************************/
			/* [9] Translate TEMPs to registers ... */
			/****************************************/
			IR.getInstance().createTemporariesMapping();
			
			/************************/
			/* [10] MIPS the IR ... */
			/************************/
			IR.getInstance().MIPSme();

			/**************************************/
			/* [11] Finalize AST GRAPHIZ DOT file */
			/**************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();			

			/***************************/
			/* [12] Finalize MIPS file */
			/***************************/
			MIPSGenerator.getInstance().finalizeFile();			

			/**************************/
			/* [13] Close output file */
			/**************************/
			file_writer.close();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


