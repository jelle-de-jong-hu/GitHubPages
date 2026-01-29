package nl.hu.s4.project.lingo.domain.exception;

public class InvalidWordLengthException extends RuntimeException {
    public InvalidWordLengthException(String solution, int expectedLength) {
        super("The word '" + solution + "' has an invalid length. Expected length: " + expectedLength + ".");
    }
}
