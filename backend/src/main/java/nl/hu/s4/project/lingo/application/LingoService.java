package nl.hu.s4.project.lingo.application;

import jakarta.transaction.Transactional;
import nl.hu.s4.project.lingo.data.GameRepository;
import nl.hu.s4.project.lingo.domain.Attempt;
import nl.hu.s4.project.lingo.domain.Lingo;
import nl.hu.s4.project.words.application.WordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LingoService {
    private final WordService wordService;
    private final GameRepository gameRepository;

    public LingoService(WordService wordService, GameRepository gameRepository) {
        this.wordService = wordService;
        this.gameRepository = gameRepository;
    }

    public Progress startNewGame() {
        String solution = wordService.provideRandomWord(Lingo.MIN_WORD_LENGTH);
        Lingo lingo = Lingo.startGame(solution);
        gameRepository.save(lingo);

        return mapToProgress(lingo);
    }

    public Progress startNewRound(long gameId) {
        Lingo lingo = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        String solution = wordService.provideRandomWord(lingo.getWordLengthForNextRound());
        lingo.startNewRound(solution);

        return mapToProgress(lingo);
    }

    public Progress guess(long gameId, String attempt) {
        boolean wordExists = wordService.wordExists(attempt);
        Attempt attemptObj = new Attempt(attempt, wordExists);

        Lingo lingo = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));

        lingo.guessWord(attemptObj);

        return mapToProgress(lingo);
    }

    public List<Progress> getGames() {
        List<Lingo> lingos = gameRepository.findAll();
        return lingos.stream().map(this::mapToProgress).toList();
    }

    public Progress getGameById(long gameId) {
        Lingo lingo = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        return mapToProgress(lingo);
    }

    private Progress mapToProgress(Lingo lingo) {
        return new Progress(
                lingo.getId(),
                lingo.getRoundNumber(),
                lingo.getSolution(),
                lingo.getAttemptCount(),
                lingo.getStatus(),
                lingo.getScore(),
                lingo.getFeedback(),
                lingo.getHint()
        );
    }
}
