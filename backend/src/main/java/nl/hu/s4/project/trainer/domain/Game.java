package nl.hu.s4.project.trainer.domain;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private long id;
    private List<Round> rounds;
    private GameStatus status;

    Game() {
    }

    public static Game startGame() {
        Game game = new Game();
        game.status = GameStatus.NEW;
        game.rounds = new ArrayList<Round>();
        return game;
    }

    public int getScore() {
        return 0;
    }

    public Progress guessWord(Attempt attempt) {
        rounds.getLast().guess(attempt);
        return null;
    }


}
