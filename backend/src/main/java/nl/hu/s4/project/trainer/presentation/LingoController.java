package nl.hu.s4.project.trainer.presentation;

import nl.hu.s4.project.trainer.application.GameNotFoundException;
import nl.hu.s4.project.trainer.application.LingoTrainerService;
import nl.hu.s4.project.trainer.application.Progress;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/trainer")
public class LingoController {

    private final LingoTrainerService trainerService;

    public LingoController(LingoTrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    public Progress startNewGame() {
        return trainerService.startNewGame();
    }

    @PatchMapping("/{gameId}")
    public Progress guess(@PathVariable long gameId, @RequestBody Attempt attempt) {
        try {
            return trainerService.guess(gameId, attempt.attempt());
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/{gameId}/round")
    public Progress startNewRound(@PathVariable long gameId) {
        try {
            return trainerService.startNewRound(gameId);
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{gameId}")
    public Progress findGameById(@PathVariable long gameId) {
        try {
            return trainerService.getGameById(gameId);
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/")
    public List<Progress> findAllGames() {
        return trainerService.getGames();
    }

}
