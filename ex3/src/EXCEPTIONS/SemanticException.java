package EXCEPTIONS;

public class SemanticException extends Exception{
    public int lineNumber;

    public SemanticException(String errorMessage, int lineNumber)
    {
        super(errorMessage);
        this.lineNumber = lineNumber;
    }
}
