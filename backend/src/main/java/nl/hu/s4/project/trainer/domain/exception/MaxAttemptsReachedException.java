package nl.hu.s4.project.trainer.domain.exception;

public class MaxAttemptsReachedException extends RuntimeException{
    public MaxAttemptsReachedException() {
        super("Maximum number of attempts reached for round.");
    }
}
