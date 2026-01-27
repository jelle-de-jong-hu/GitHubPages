package nl.hu.s4.project.trainer.domain.exception;

public class InvalidWordLengthException extends RuntimeException {
    public InvalidWordLengthException(String solution, int expectedLength) {
        super("The word '" + solution + "' has an invalid length. Expected length: " + expectedLength + ".");
    }
}
