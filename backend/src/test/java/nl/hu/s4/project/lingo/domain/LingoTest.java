package nl.hu.s4.project.lingo.domain;

import nl.hu.s4.project.lingo.domain.exception.GameOverException;
import nl.hu.s4.project.lingo.domain.exception.InvalidWordLengthException;
import nl.hu.s4.project.lingo.domain.exception.NoActiveRoundException;
import nl.hu.s4.project.lingo.domain.exception.RoundAlreadyStartedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Lingo")
class LingoTest {

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
            Lingo lingo = Lingo.startGame(FIVE_LETTER_ATTEMPT);

            assertEquals(GameStatus.IN_PROGRESS, lingo.getStatus());
            assertEquals(1, lingo.getRoundNumber());
            assertEquals(0, lingo.getAttemptCount());
            assertEquals("", lingo.getSolution());
        }
    }


    @Nested
    @DisplayName("getWordLengthForNextRound")
    class WordLengthTests {

        @Test
        @DisplayName("getWordLengthForNextRound berekent juiste woordlengte per ronde: 5 -> 6")
        void getWordLengthForNextRound_variesWithRoundCount_5_6() {
            Lingo lingo = Lingo.startGame(FIVE_LETTER_ATTEMPT);
            lingo.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));

            assertEquals(6, lingo.getWordLengthForNextRound());
        }

        @Test
        @DisplayName("getWordLengthForNextRound berekent juiste woordlengte per ronde: 6 -> 7")
        void getWordLengthForNextRound_variesWithRoundCount_6_7() {
            Lingo lingo = Lingo.startGame(FIVE_LETTER_ATTEMPT);
            lingo.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));
            lingo.startNewRound(SIX_LETTER_ATTEMPT);
            lingo.guessWord(validAttempt(SIX_LETTER_ATTEMPT));

            assertEquals(7, lingo.getWordLengthForNextRound());
        }

        @Test
        @DisplayName("getWordLengthForNextRound berekent juiste woordlengte per ronde: 7 -> 5")
        void getWordLengthForNextRound_variesWithRoundCount_7_5() {
            Lingo lingo = Lingo.startGame(FIVE_LETTER_ATTEMPT);
            lingo.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));
            lingo.startNewRound(SIX_LETTER_ATTEMPT);
            lingo.guessWord(validAttempt(SIX_LETTER_ATTEMPT));
            lingo.startNewRound(SEVEN_LETTER_ATTEMPT);
            lingo.guessWord(validAttempt(SEVEN_LETTER_ATTEMPT));

            assertEquals(5, lingo.getWordLengthForNextRound());
        }
    }

    @Nested
    @DisplayName("startNewRound")
    class StartNewRoundTests {

        @Test
        @DisplayName("startNewRound tijdens actieve ronde gooit RoundAlreadyStartedException")
        void startNewRound_whileInProgress_throwsException() {
            Lingo lingo = Lingo.startGame(FIVE_LETTER_ATTEMPT);

            assertThrows(RoundAlreadyStartedException.class,
                    () -> lingo.startNewRound(FIVE_LETTER_ATTEMPT));
        }

        @Test
        @DisplayName("startNewRound na gewonnen ronde met verkeerde lengte gooit InvalidWordLengthException")
        void startNewRound_withInvalidWordLength_throwsException() {
            Lingo lingo = Lingo.startGame(FIVE_LETTER_ATTEMPT);

            lingo.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));

            // next length is 6, geef iets anders (5)
            assertThrows(InvalidWordLengthException.class,
                    () -> lingo.startNewRound(FIVE_LETTER_ATTEMPT));
        }

        @Test
        @DisplayName("startNewRound na gewonnen ronde start nieuwe ronde correct")
        void startNewRound_afterWin_startsNewRound() {
            Lingo lingo = Lingo.startGame(FIVE_LETTER_ATTEMPT);
            lingo.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));
            lingo.startNewRound(SIX_LETTER_ATTEMPT);
            assertEquals(GameStatus.IN_PROGRESS, lingo.getStatus());
        }

        @Nested
        @DisplayName("guessWord")
        class GuessWordTests {

            @Test
            @DisplayName("correcte gok zet status op LAST_ROUND_WON en solution wordt zichtbaar")
            void correctGuess_winsRound() {
                Lingo lingo = Lingo.startGame(FIVE_LETTER_ATTEMPT);

                lingo.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));

                assertEquals(GameStatus.LAST_ROUND_WON, lingo.getStatus());
                assertEquals(FIVE_LETTER_ATTEMPT, lingo.getSolution());
            }

            @Test
            @DisplayName("guessWord na gewonnen ronde gooit NoActiveRoundException")
            void guessWord_afterWin_throwsNoActiveRound() {
                Lingo lingo = Lingo.startGame("apple");
                lingo.guessWord(validAttempt("apple"));

                assertThrows(NoActiveRoundException.class,
                        () -> lingo.guessWord(validAttempt("apple")));
            }

            @Test
            @DisplayName("guessWord na verloren spel gooit GameOverException")
            void guessWord_afterGameOver_throwsGameOver() {
                Lingo lingo = Lingo.startGame("apple");

                // Forceer verlies: herhaaldelijk fout (maar wel 'valid') gokken tot status != IN_PROGRESS
                while (lingo.getStatus() == GameStatus.IN_PROGRESS) {
                    lingo.guessWord(validAttempt("wrong"));
                }

                assertEquals(GameStatus.LOST, lingo.getStatus());

                assertThrows(GameOverException.class,
                        () -> lingo.guessWord(validAttempt("apple")));
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
                Lingo lingo = Lingo.startGame(FIVE_LETTER_ATTEMPT);

                for (int i = 0; i < wrongAttempts; i++) {
                    lingo.guessWord(validAttempt("wrong"));
                }
                lingo.guessWord(validAttempt(FIVE_LETTER_ATTEMPT));

                assertEquals(GameStatus.LAST_ROUND_WON, lingo.getStatus());
                assertEquals(expectedScore, lingo.getScore());
            }
        }

        @Test
        @DisplayName("Score wordt opgeteld over meerdere rondes")
        void score_accumulatesOverRounds() {
            Lingo lingo = Lingo.startGame(FIVE_LETTER_ATTEMPT);
            lingo.guessWord(validAttempt(FIVE_LETTER_ATTEMPT)); // +25
            lingo.startNewRound(SIX_LETTER_ATTEMPT);
            lingo.guessWord(validAttempt(SIX_LETTER_ATTEMPT)); // +25
            assertEquals(50, lingo.getScore());
        }
    }
}
