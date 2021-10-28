import java.io.*;
import java.io.PrintWriter;
import java.util.ArrayList;

import java_cup.runtime.Symbol;

public class Main
{
	static private String build_output_msg(Symbol s, int line, int pos)
	{
		String token_name = TokenNames.getTokenName(s.sym);
		if (token_name == null)
		{
			throw new IllegalStateException("Tried to access invalid token index: " + s.sym);
		}
		StringBuilder sb = new StringBuilder(token_name);

		if ((s.sym == TokenNames.INT) || (s.sym == TokenNames.ID))
		{
			sb.append('(');
			sb.append(s.value);
			sb.append(')');
		}
		else if (s.sym == TokenNames.STRING)
		{
			sb.append("(\"");
			sb.append(s.value);
			sb.append("\")");
		}

		sb.append('[');
		sb.append(line);
		sb.append(',');
		sb.append(pos);
		sb.append("]\n");

		return sb.toString();
	}

	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		ArrayList<String> output = new ArrayList<>();
		boolean error = false;

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

			/***********************/
			/* [4] Read next token */
			/***********************/
			s = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			while (s.sym != TokenNames.EOF.getOrdinal())
			{
				/************************/
				/* [6] Print to console */
				/************************/
				System.out.print("[");
				System.out.print(l.getLine());
				System.out.print(",");
				System.out.print(l.getTokenStartPosition());
				System.out.print("]:");
				System.out.print(s.value);
				System.out.print("\n");

				/*********************/
				/* [7] Print to file */
				/*********************/
				if (s.sym == TokenNames.ERROR)
				{
					file_writer.print("ERROR\n");
					error = true;
					break;
				}
				output.add(build_output_msg(s, l.getLine(), l.getTokenStartPosition()));

				/***********************/
				/* [8] Read next token */
				/***********************/
				s = l.next_token();
			}

			if (!error)
			{
				for (String st: output)
				{
					file_writer.print(st);
				}
			}

			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			file_writer.close();
    	}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


