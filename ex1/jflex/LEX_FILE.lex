/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/

	private Symbol symbol(int type)
	{ // creates a general symbol (e.g. PLUS)
	    return new Symbol(type, yyline, yycolumn);
    }

	private Symbol symbol(int type, Object value)
	{ // Creates a general symbol with value (e.g. ID, String)
	    if(type == TokenNames.INT)
	    { // special treatment for integer
	        int lower_bound = 0, upper_bound = (1 << 15) - 1;
            if((Integer) value < lower_bound || (Integer) value > upper_bound)
            {
                return new Symbol(TokenNames.ERROR, yyline, yycolumn, value);
            }
	    }
	    return new Symbol(type, yyline, yycolumn, value);
	}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; } 

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/

/* Base Macros */
/* TODO is \r a LineTerminator*/
LineTerminator	= \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
Letters = [a-zA-Z]
Digits = [0-9]

/* Symbols Macros */
Identifiers = {Letters}({Letters}|{Digits})*
Integers = 0|[1-9][0-9]*
Strings = \"{Letters}*\"


/* Comments Macros */

CharInOneLineComments = [\(\)\[\]\{\}\?!\+\-\*\/\.;]|{Letters}|{Digits}|[ \t\f]
OneLineComment = \/\/{CharInOneLineComments}*


CharInMultiCommentWithoutAsteriskAndSlash = [\(\)\[\]\{\}\?!\+\-\.;]|{Letters}|{Digits}|{WhiteSpace}

ValidInMCommentsWoAsterisk = {CharInMultiCommentWithoutAsteriskAndSlash}|\/
AsteriskWithSomethingAfterNotSlash = \*{CharInMultiCommentWithoutAsteriskAndSlash}|\*
InsideMultiComment = ({ValidInMCommentsWoAsterisk}|{AsteriskWithSomethingAfterNotSlash})*\*?


MultiLineComments = \/\*{InsideMultiComment}*\*\/

Comments = {OneLineComment}|{MultiLineComments}

ErrorComment = \/\*{InsideMultiComment}


/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {

// Keywords should apear BEFORE Identifiers

/* Keywords */
"class"			{ return symbol(TokenNames.CLASS);}
"nil"				{ return symbol(TokenNames.NIL);}
"array"			{ return symbol(TokenNames.ARRAY);}
"while"			{ return symbol(TokenNames.WHILE);}
"int"				{ return symbol(TokenNames.TYPE_INT);}
"extends"		{ return symbol(TokenNames.EXTENDS);}
"return"		{ return symbol(TokenNames.RETURN);}
"new"				{ return symbol(TokenNames.NEW);}
"if"				{ return symbol(TokenNames.IF);}
"string"		{ return symbol(TokenNames.TYPE_STRING);}


"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
"["					{ return symbol(TokenNames.LBRACK);}
"]"					{ return symbol(TokenNames.RBRACK);}
"{"					{ return symbol(TokenNames.LBRACE);}
"}"					{ return symbol(TokenNames.RBRACE);}
"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
"*"					{ return symbol(TokenNames.TIMES);}
"/"					{ return symbol(TokenNames.DIVIDE);}
","					{ return symbol(TokenNames.COMMA);}
"."					{ return symbol(TokenNames.DOT);}
";"					{ return symbol(TokenNames.SEMICOLON);}
":="				{ return symbol(TokenNames.ASSIGN);}
"="					{ return symbol(TokenNames.EQ);}
"<"					{ return symbol(TokenNames.LT);}
">"					{ return symbol(TokenNames.GT);}


{Identifiers}		{ return symbol(TokenNames.ID, new String(yytext()));}  
{Integers}			{ return symbol(TokenNames.INT, new Integer(yytext()));}
{Strings}			{ return symbol(TokenNames.STRING, new String(yytext()));}  
{WhiteSpace}		{ /* just skip what was found, do nothing */ }
{Comments}			{ /* just skip what was found, do nothing */ }
{ErrorComment}		{ return symbol(TokenNames.ERROR);}
<<EOF>>				{ return symbol(TokenNames.EOF);}
}
