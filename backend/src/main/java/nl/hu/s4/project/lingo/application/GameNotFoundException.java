package nl.hu.s4.project.lingo.application;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long id) {
        super("Game with id " + id + " not found.");
    }
}
