public interface TokenNames {
  // terminals
  public static final int EOF = 0;
  public static final int LPAREN    = 1;
  public static final int RPAREN    = 2;
  public static final int LBRACK    = 3;
  public static final int RBRACK  = 4;
  public static final int LBRACE  = 5;
  public static final int RBRACE  = 6;
  public static final int NIL     = 7;
  public static final int PLUS    = 8;
  public static final int MINUS   = 9;
  public static final int TIMES   = 10;
  public static final int DIVIDE  = 11;
  public static final int COMMA   = 12;
  public static final int DOT     = 13;
  public static final int SEMICOLON  = 14;
  public static final int ASSIGN    = 15;
  public static final int EQ    = 16;
  public static final int LT    = 17;
  public static final int GT    = 18;
  public static final int ARRAY   = 19;
  public static final int CLASS   = 20;
  public static final int EXTENDS   = 21;
  public static final int RETURN   = 22;
  public static final int WHILE   = 23;
  public static final int IF   = 24;
  public static final int NEW   = 25;
  public static final int NUMBER   = -1; // todo delete me


  // Symbols with value
  public static final int INT   = 26;
  public static final int STRING   = 27;
  public static final int ID   = 28;

  // Symbols for ERRORs
  // Our convention is - [Token-type-number > ERROR] IFF [A lexical error occurred]
  public static final int ERROR  = 29;
  public static final int ERROR_BIG_INTEGER  = 30;
}
