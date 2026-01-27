package nl.hu.s4.project.trainer.application;

import jakarta.transaction.Transactional;
import nl.hu.s4.project.trainer.data.GameRepository;
import nl.hu.s4.project.trainer.domain.Attempt;
import nl.hu.s4.project.trainer.domain.LingoTrainer;
import nl.hu.s4.project.words.application.WordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LingoTrainerService {
    private final WordService wordService;
    private final GameRepository gameRepository;

    public LingoTrainerService(WordService wordService, GameRepository gameRepository) {
        this.wordService = wordService;
        this.gameRepository = gameRepository;
    }

    public Progress startNewGame() {
        String solution = wordService.provideRandomWord(LingoTrainer.MIN_WORD_LENGTH);
        LingoTrainer lingoTrainer = LingoTrainer.startGame(solution);
        gameRepository.save(lingoTrainer);

        return mapToProgress(lingoTrainer);
    }

    public Progress startNewRound(long gameId) {
        LingoTrainer lingoTrainer = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        String solution = wordService.provideRandomWord(lingoTrainer.getWordLengthForNextRound());
        lingoTrainer.startNewRound(solution);

        return mapToProgress(lingoTrainer);
    }

    public Progress guess(long gameId, String attempt) {
        boolean wordExists = wordService.wordExists(attempt);
        Attempt attemptObj = new Attempt(attempt, wordExists);

        LingoTrainer lingoTrainer = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));

        lingoTrainer.guessWord(attemptObj);

        return mapToProgress(lingoTrainer);
    }

    public List<Progress> getGames() {
        List<LingoTrainer> lingoTrainers = gameRepository.findAll();
        return lingoTrainers.stream().map(this::mapToProgress).toList();
    }

    public Progress getGameById(long gameId) {
        LingoTrainer lingoTrainer = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        return mapToProgress(lingoTrainer);
    }

    private Progress mapToProgress(LingoTrainer lingoTrainer) {
        return new Progress(
                lingoTrainer.getId(),
                lingoTrainer.getRoundNumber(),
                lingoTrainer.getSolution(),
                lingoTrainer.getAttemptCount(),
                lingoTrainer.getStatus(),
                lingoTrainer.getScore(),
                lingoTrainer.getFeedback(),
                lingoTrainer.getHint()
        );
    }
}
