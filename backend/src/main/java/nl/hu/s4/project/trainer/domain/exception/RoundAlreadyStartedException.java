package nl.hu.s4.project.trainer.domain.exception;

public class RoundAlreadyStartedException extends RuntimeException {
    public RoundAlreadyStartedException(Long id) {
        super("Round with id " + id + " has already started.");
    }
}
