package nl.hu.s4.project.trainer.domain;

import nl.hu.s4.project.trainer.domain.exception.GameOverException;
import nl.hu.s4.project.trainer.domain.exception.InvalidWordLengthException;
import nl.hu.s4.project.trainer.domain.exception.NoActiveRoundException;
import nl.hu.s4.project.trainer.domain.exception.RoundAlreadyStartedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("LingoTrainer")
class LingoTrainerTest {

    private static Attempt validAttempt(String word) {
        return new Attempt(word, true);
    }

    private static final String FIVE_LETTER_ATTEMPT = "raden";
    private static final String SIX_LETTER_ATTEMPT = "aalbes";
    private static final String SEVEN_LETTER_ATTEMPT = "aalmoes";

    @Nested
    @DisplayName("startGame")
    class StartGameTests {

        @Test
        @DisplayName("startGame zet status IN_PROGRESS en start met één ronde")
        void startGame_setsInitialState() {
            LingoTrainer trainer = LingoTrainer.startGame(FIVE_LETTER_ATTEMPT);

            assertEquals(GameStatus.IN_PROGRESS, trainer.getStatus());
            assertEquals(1, trainer.getRoundNumber());
            assertEquals(0, trainer.getAttemptCount());
            assertEquals("", trainer.getSolution());
        }
    }


    @Nested
    @DisplayName("getWordLengthForNextRound")
    class WordLengthTests {

        @Test
        @DisplayName("getWordLengthForNextRound berekent juiste woordlengte per ronde: 5 -> 6")
        void getWordLengthForNextRound_variesWithRoundCount_5_6() {
            LingoTrainer trainer = LingoTrainer.startGame(FIVE_LETTER_ATTEMPT);
            trainer.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));

            assertEquals(6, trainer.getWordLengthForNextRound());
        }

        @Test
        @DisplayName("getWordLengthForNextRound berekent juiste woordlengte per ronde: 6 -> 7")
        void getWordLengthForNextRound_variesWithRoundCount_6_7() {
            LingoTrainer trainer = LingoTrainer.startGame(FIVE_LETTER_ATTEMPT);
            trainer.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));
            trainer.startNewRound(SIX_LETTER_ATTEMPT);
            trainer.guessWord(validAttempt(SIX_LETTER_ATTEMPT));

            assertEquals(7, trainer.getWordLengthForNextRound());
        }

        @Test
        @DisplayName("getWordLengthForNextRound berekent juiste woordlengte per ronde: 7 -> 5")
        void getWordLengthForNextRound_variesWithRoundCount_7_5() {
            LingoTrainer trainer = LingoTrainer.startGame(FIVE_LETTER_ATTEMPT);
            trainer.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));
            trainer.startNewRound(SIX_LETTER_ATTEMPT);
            trainer.guessWord(validAttempt(SIX_LETTER_ATTEMPT));
            trainer.startNewRound(SEVEN_LETTER_ATTEMPT);
            trainer.guessWord(validAttempt(SEVEN_LETTER_ATTEMPT));

            assertEquals(5, trainer.getWordLengthForNextRound());
        }
    }

    @Nested
    @DisplayName("startNewRound")
    class StartNewRoundTests {

        @Test
        @DisplayName("startNewRound tijdens actieve ronde gooit RoundAlreadyStartedException")
        void startNewRound_whileInProgress_throwsException() {
            LingoTrainer trainer = LingoTrainer.startGame(FIVE_LETTER_ATTEMPT);

            assertThrows(RoundAlreadyStartedException.class,
                    () -> trainer.startNewRound(FIVE_LETTER_ATTEMPT));
        }

        @Test
        @DisplayName("startNewRound na gewonnen ronde met verkeerde lengte gooit InvalidWordLengthException")
        void startNewRound_withInvalidWordLength_throwsException() {
            LingoTrainer trainer = LingoTrainer.startGame(FIVE_LETTER_ATTEMPT);

            trainer.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));

            // next length is 6, geef iets anders (5)
            assertThrows(InvalidWordLengthException.class,
                    () -> trainer.startNewRound(FIVE_LETTER_ATTEMPT));
        }

        @Test
        @DisplayName("startNewRound na gewonnen ronde start nieuwe ronde correct")
        void startNewRound_afterWin_startsNewRound() {
            LingoTrainer trainer = LingoTrainer.startGame(FIVE_LETTER_ATTEMPT);
            trainer.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));
            trainer.startNewRound(SIX_LETTER_ATTEMPT);
            assertEquals(GameStatus.IN_PROGRESS, trainer.getStatus());
        }

        @Nested
        @DisplayName("guessWord")
        class GuessWordTests {

            @Test
            @DisplayName("correcte gok zet status op LAST_ROUND_WON en solution wordt zichtbaar")
            void correctGuess_winsRound() {
                LingoTrainer trainer = LingoTrainer.startGame(FIVE_LETTER_ATTEMPT);

                trainer.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));

                assertEquals(GameStatus.LAST_ROUND_WON, trainer.getStatus());
                assertEquals(FIVE_LETTER_ATTEMPT, trainer.getSolution());
            }

            @Test
            @DisplayName("guessWord na gewonnen ronde gooit NoActiveRoundException")
            void guessWord_afterWin_throwsNoActiveRound() {
                LingoTrainer trainer = LingoTrainer.startGame("apple");
                trainer.guessWord(validAttempt("apple"));

                assertThrows(NoActiveRoundException.class,
                        () -> trainer.guessWord(validAttempt("apple")));
            }

            @Test
            @DisplayName("guessWord na verloren spel gooit GameOverException")
            void guessWord_afterGameOver_throwsGameOver() {
                LingoTrainer trainer = LingoTrainer.startGame("apple");

                // Forceer verlies: herhaaldelijk fout (maar wel 'valid') gokken tot status != IN_PROGRESS
                while (trainer.getStatus() == GameStatus.IN_PROGRESS) {
                    trainer.guessWord(validAttempt("wrong"));
                }

                assertEquals(GameStatus.LOST, trainer.getStatus());

                assertThrows(GameOverException.class,
                        () -> trainer.guessWord(validAttempt("apple")));
            }
        }

        @Nested
        @DisplayName("score")
        class ScoreTests {

            static Stream<Arguments> scoreExamples() {
                return Stream.of(
                        Arguments.of(0, 25),
                        Arguments.of(1, 20),
                        Arguments.of(2, 15),
                        Arguments.of(3, 10),
                        Arguments.of(4, 5)
                );
            }

            @ParameterizedTest
            @MethodSource("scoreExamples")
            @DisplayName("score is positief na gewonnen ronde (ongeacht aantal pogingen)")
            void score_afterWinningRound(int wrongAttempts, int expectedScore) {
                LingoTrainer trainer = LingoTrainer.startGame(FIVE_LETTER_ATTEMPT);

                for (int i = 0; i < wrongAttempts; i++) {
                    trainer.guessWord(validAttempt("wrong"));
                }
                trainer.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));

                assertEquals(GameStatus.LAST_ROUND_WON, trainer.getStatus());
                assertEquals(expectedScore, trainer.getScore());
            }
        }

        @Test
        @DisplayName("Score wordt opgeteld over meerdere rondes")
        void score_accumulatesOverRounds() {
            LingoTrainer trainer = LingoTrainer.startGame(FIVE_LETTER_ATTEMPT);
            trainer.guessWord(validAttempt(FIVE_LETTER_ATTEMPT)); // +25
            trainer.startNewRound(SIX_LETTER_ATTEMPT);
            trainer.guessWord(validAttempt(SIX_LETTER_ATTEMPT)); // +25
            assertEquals(50, trainer.getScore());
        }
    }
}
