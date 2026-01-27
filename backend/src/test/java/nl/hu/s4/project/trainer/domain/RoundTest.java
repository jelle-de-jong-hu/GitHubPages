package nl.hu.s4.project.trainer.domain;

import nl.hu.s4.project.trainer.domain.exception.MaxAttemptsReachedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    private static final String FIVE_LETTER_ATTEMPT = "raden";

    @Test
    void newRound_isNotSolved_andNotLost() {
        Round round = new Round(FIVE_LETTER_ATTEMPT);

        assertFalse(round.isSolved());
        assertFalse(round.isLost());
        assertEquals(0, round.calculateScore());
    }

    @Test
    void guess_addsFeedbackAttempt() {
        Round round = new Round(FIVE_LETTER_ATTEMPT);

        round.guess(new Attempt("ramen", true));

        assertEquals(1, round.getAttempts().size());
        assertNotNull(round.getAttempts().getFirst().getMarks());
    }

    @Test
    void isSolved_true_whenLastAttemptSolved() {
        Round round = new Round(FIVE_LETTER_ATTEMPT);

        round.guess(new Attempt("ramen", true));
        assertFalse(round.isSolved());

        round.guess(new Attempt(FIVE_LETTER_ATTEMPT, true));
        assertTrue(round.isSolved());
    }

    @Test
    void isLost_true_afterMaxAttempts_whenNotSolved() {
        Round round = new Round(FIVE_LETTER_ATTEMPT);

        round.guess(new Attempt("aaaaa", true));
        round.guess(new Attempt("bbbbb", true));
        round.guess(new Attempt("ccccc", true));
        round.guess(new Attempt("ddddd", true));
        round.guess(new Attempt("eeeee", true));

        assertFalse(round.isSolved());
        assertTrue(round.isLost());
        assertEquals(0, round.calculateScore());
    }

    @Test
    void guess_throwsAfterMaximumAttempts() {
        Round round = new Round(FIVE_LETTER_ATTEMPT);

        round.guess(new Attempt("aaaaa", true));
        round.guess(new Attempt("bbbbb", true));
        round.guess(new Attempt("ccccc", true));
        round.guess(new Attempt("ddddd", true));
        round.guess(new Attempt("eeeee", true));

        assertThrows(MaxAttemptsReachedException.class,
                () -> round.guess(new Attempt(FIVE_LETTER_ATTEMPT, true)));
    }

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
    void calculateScore_variesWithNumberOfAttempts(int attemptsMade, int expectedScore) {
        Round round = new Round(FIVE_LETTER_ATTEMPT);

        for (int i = 0; i < attemptsMade; i++) {
            round.guess(new Attempt("aaaaa", true));
        }
        round.guess(new Attempt(FIVE_LETTER_ATTEMPT, true));

        assertTrue(round.isSolved());
        assertEquals(expectedScore, round.calculateScore());
    }


    @Test
    void generateHint_alwaysRevealsFirstLetter() {
        Round round = new Round("raden");
        assertArrayEquals(new String[]{"r", "_", "_", "_", "_"}, round.generateHint());
    }

    @Test
    void generateHint_alwaysRevealsFirst_And_Other_Correct_Attempts() {
        Round round = new Round("raden");
        round.guess(new Attempt("roken", true));

        assertArrayEquals(new String[]{"r", "_", "_", "e", "n"}, round.generateHint());
    }

    @Test
    void generateHint_alwaysRevealsFirst_And_Former_Correct_Letters() {
        Round round = new Round("raden");
        round.guess(new Attempt("roken", true));
        round.guess(new Attempt("robot", true));

        assertArrayEquals(new String[]{"r", "_", "_", "e", "n"}, round.generateHint());
    }



    @Test
    void round_constructor_lowercasesSolution() {
        Round round = new Round("CIGAR");
        assertEquals("cigar", round.getSolution());
    }
}
