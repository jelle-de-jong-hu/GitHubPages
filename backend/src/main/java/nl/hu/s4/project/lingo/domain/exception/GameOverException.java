package nl.hu.s4.project.lingo.domain.exception;

public class GameOverException extends RuntimeException {
    public GameOverException(Long id) {
        super("Game with id " + id + " is over.");
    }
}
