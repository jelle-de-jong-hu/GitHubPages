package nl.hu.s4.project.lingo.presentation;

import nl.hu.s4.project.lingo.application.LingoService;
import nl.hu.s4.project.lingo.application.Progress;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class LingoController {

    private final LingoService lingoService;

    public LingoController(LingoService lingoService) {
        this.lingoService = lingoService;
    }

    public Progress startNewGame() {
        return lingoService.startNewGame();
    }

    public Progress guess(long gameId, Attempt attempt) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    public Progress startNewRound(long gameId) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    public Progress findGameById(long gameId) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    public List<Progress> findAllGames() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
