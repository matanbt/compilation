public enum TokenNames
{
  /*
   * Enum class for all the tokens.
   * Note: When integrating with Lex, we'll identify tokens with their ORDINAL (integer).
   *       For example when we call Symbol constructor (in LEX_FILE) we shall use TokenNames.STRING.getOrdinal().
   */
  EOF, LPAREN, RPAREN, LBRACK, RBRACK, LBRACE, RBRACE,
  NIL, PLUS, MINUS, TIMES, DIVIDE, COMMA, DOT,
  SEMICOLON, ASSIGN, EQ, LT, GT, ARRAY, CLASS,
  EXTENDS, RETURN, WHILE, IF, NEW,
  INT, STRING, ID, ERROR;

  public int getOrdinal() {
    // returns the ordinal number of the Token instance
    return this.ordinal();
  }

  public String getStringFromOrdinal(int ord) {
    // given a token-ordinal returns its string representation
    return TokenNames.values()[ord].toString();
  }

}
