package nl.hu.s4.project.lingo.domain;

import jakarta.persistence.*;
import nl.hu.s4.project.lingo.domain.exception.GameOverException;
import nl.hu.s4.project.lingo.domain.exception.InvalidWordLengthException;
import nl.hu.s4.project.lingo.domain.exception.NoActiveRoundException;
import nl.hu.s4.project.lingo.domain.exception.RoundAlreadyStartedException;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Lingo {

    private static final int MAX_WORD_LENGTH = 7;
    public static final int MIN_WORD_LENGTH = 5;

    @Id
    @GeneratedValue
    private long id;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<Round> rounds;
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    public Lingo() {
    }

    public static Lingo startGame(String solution) {
        Lingo lingo = new Lingo();
        lingo.status = GameStatus.IN_PROGRESS;
        lingo.rounds = new ArrayList<>();
        lingo.rounds.add(new Round(solution));
        return lingo;
    }

    public long getId() {
        return id;
    }

    public void startNewRound(String solution) {
        if (status == GameStatus.IN_PROGRESS) {
            throw new RoundAlreadyStartedException(id);
        }
        if (status == GameStatus.LOST) {
            throw new GameOverException(id);
        }

        if (solution.length() != getWordLengthForNextRound()) {
            throw new InvalidWordLengthException(solution, getWordLengthForNextRound());
        }

        rounds.add(new Round(solution));
        status = GameStatus.IN_PROGRESS;
    }

    public int getWordLengthForNextRound() {
        return (rounds.size() % (MAX_WORD_LENGTH + 1 - MIN_WORD_LENGTH)) + MIN_WORD_LENGTH;
    }


    public void guessWord(Attempt attempt) {
        if (status == GameStatus.LOST) {
            throw new GameOverException(id);
        }

        if(status != GameStatus.IN_PROGRESS) {
            throw new NoActiveRoundException(id);
        }

        Round currentRound = rounds.getLast();
        currentRound.guess(attempt);

        if (currentRound.isSolved()) {
            status = GameStatus.LAST_ROUND_WON;
        } else if (currentRound.isLost()) {
            status = GameStatus.LOST;
        } else {
            status = GameStatus.IN_PROGRESS;
        }

    }

    public int getRoundNumber() {
        return rounds.size();
    }

    public String getSolution() {
        if (status.equals(GameStatus.LAST_ROUND_WON) || status.equals(GameStatus.LOST)) {
            return rounds.getLast().getSolution();
        }
        return "";
    }

    public int getAttemptCount() {
        return rounds.getLast().getAttempts().size();
    }

    public GameStatus getStatus() {
        return status;
    }

    public int getScore() {
        return rounds.stream().mapToInt(Round::calculateScore).sum();
    }

    public List<Feedback> getFeedback() {
        return rounds.getLast().getAttempts();
    }

    public String[] getHint() {
        return rounds.getLast().generateHint();
    }
}
