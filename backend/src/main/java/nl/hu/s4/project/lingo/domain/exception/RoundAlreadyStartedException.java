package nl.hu.s4.project.lingo.domain.exception;

public class RoundAlreadyStartedException extends RuntimeException {
    public RoundAlreadyStartedException(Long id) {
        super("Round with id " + id + " has already started.");
    }
}
