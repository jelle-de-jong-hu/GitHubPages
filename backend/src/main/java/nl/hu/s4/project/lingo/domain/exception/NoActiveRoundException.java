package nl.hu.s4.project.lingo.domain.exception;

public class NoActiveRoundException extends RuntimeException {
    public NoActiveRoundException(Long gameId) {
        super("No round in progress for game with id: " + gameId + ". Start a new round first.");
    }
}
