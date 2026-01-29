package nl.hu.s4.project.lingo.presentation;

import nl.hu.s4.project.lingo.application.GameNotFoundException;
import nl.hu.s4.project.lingo.application.LingoService;
import nl.hu.s4.project.lingo.application.Progress;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/lingo")
public class LingoController {

    private final LingoService lingoService;

    public LingoController(LingoService lingoService) {
        this.lingoService = lingoService;
    }

    @PostMapping
    public Progress startNewGame() {
        return lingoService.startNewGame();
    }

    @PatchMapping("/{gameId}")
    public Progress guess(@PathVariable long gameId, @RequestBody Attempt attempt) {
        try {
            return lingoService.guess(gameId, attempt.attempt());
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/{gameId}/round")
    public Progress startNewRound(@PathVariable long gameId) {
        try {
            return lingoService.startNewRound(gameId);
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{gameId}")
    public Progress findGameById(@PathVariable long gameId) {
        try {
            return lingoService.getGameById(gameId);
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("")
    public List<Progress> findAllGames() {
        return lingoService.getGames();
    }

}
