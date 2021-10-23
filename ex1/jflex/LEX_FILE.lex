/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;
import java.lang.Math;

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

	private Symbol symbol(TokenNames token_name)
	{ // creates a general symbol (e.g. PLUS)
	    return new Symbol(token_name.getOrdinal(), yyline, yycolumn);
    }
	private Symbol symbol(TokenNames token_name, Object value)
	{ // Creates a general symbol with value (e.g. ID, String)
	    if(token_name == TokenNames.INT)
	    { // special treatment for integer
	        int lower_bound = 0, upper_bound = Math.pow(2, 15) - 1;
            if(value < lower_bound || value > upper_bound)
            {
                return new Symbol(TokenNames.ERROR.getOrdinal(), yyline, yycolumn, value);
            }
	    }
	    return new Symbol(token_name.getOrdinal(), yyline, yycolumn, value);
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
LineTerminator	= \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
Letters = [a-zA-Z]
Digits = [0-9]

/* Symbols Macros */
Identifiers = {Letters}(Letters|Digits)*
Keywords = class|nil|array|while|extends|return|new|if
Integers = 0|[1-9][0-9]*
Strings = "{Letters}*"

/* Comments Macros */
// TODO - make sure CharInComments is correct (I added SOME missing stuff)
CharInComments = [\(\)\[\]\{\}\?!\+-\*\/\.;]|Letters|Digits|[ \t\f]
// TODO - make sure OneLineComment & MultiLineComments are correct (I split for ease of read)
OneLineComment = \/\/{CharInComments}*
MultiLineComments = \/\/{CharInComments}*|\/\*{CharInComments}*\*\/


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

// TODO - note that ALL sings in TokenNames should be added here (+,{,?,...)
"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
"PPP"				{ return symbol(TokenNames.TIMES);}
"/"					{ return symbol(TokenNames.DIVIDE);}
"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
{INTEGER}			{ return symbol(TokenNames.NUMBER, new Integer(yytext()));}
{ID}				{ return symbol(TokenNames.ID,     new String( yytext()));}   
{WhiteSpace}		{ /* just skip what was found, do nothing */ }
<<EOF>>				{ return symbol(TokenNames.EOF);}
}
