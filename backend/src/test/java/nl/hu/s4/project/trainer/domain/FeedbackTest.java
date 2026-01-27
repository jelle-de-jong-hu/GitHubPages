package nl.hu.s4.project.trainer.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    void createFeedback_marksAllInvalid_whenAttemptIsInvalid() {
        Attempt attempt = new Attempt("cigar", false);

        Feedback feedback = Feedback.createFeedback(attempt, "cigar");

        assertEquals(List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID),
                feedback.getMarks());
        assertFalse(feedback.isSolved());
    }

    @Test
    void createFeedback_marksAllInvalid_whenAttemptLengthDiffersFromSolutionLength() {
        Attempt attempt = new Attempt("too-long", true);

        Feedback feedback = Feedback.createFeedback(attempt, "cigar");

        assertEquals(List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID),
                feedback.getMarks());
        assertFalse(feedback.isSolved());
    }

    @Test
    void createFeedback_isCaseInsensitiveForAttempt() {
        Attempt attempt = new Attempt("CIGAR", true);

        Feedback feedback = Feedback.createFeedback(attempt, "cigar");

        assertEquals(List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT),
                feedback.getMarks());
        assertTrue(feedback.isSolved());
    }

    @Test
    void createFeedback_allCorrect_whenAttemptMatchesSolution() {
        Attempt attempt = new Attempt("cigar", true);

        Feedback feedback = Feedback.createFeedback(attempt, "cigar");

        assertEquals(List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT),
                feedback.getMarks());
        assertTrue(feedback.isSolved());
    }

    @Test
    void createFeedback_handlesDuplicateLetters_correctPresentAbsent() {
        // Dit test precies de "letter-telling" logica met duplicates.
        // Verwachte output volgt de implementatie in Feedback.generateMarks().
        Attempt attempt = new Attempt("allee", true);

        Feedback feedback = Feedback.createFeedback(attempt, "apple");

        assertEquals(List.of(
                        Mark.CORRECT,  // a
                        Mark.PRESENT,  // l (zit in apple, andere positie)
                        Mark.ABSENT,   // l (2e l komt niet meer "over")
                        Mark.ABSENT,   // e (zit wel in apple, maar e is op pos 4 correct -> hier niet meer beschikbaar)
                        Mark.CORRECT   // e
                ),
                feedback.getMarks());
        assertFalse(feedback.isSolved());
    }

    static Stream<Arguments> provideWordExamples() {
        return Stream.of(
                Arguments.of("banaan", "banana", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of("groep", "gedoe", List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.ABSENT)),
                Arguments.of("ksuir", "kruis", List.of(Mark.CORRECT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT, Mark.PRESENT)),
                Arguments.of("kaasje", "kastje", List.of(Mark.CORRECT, Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of("aaabbb", "bbbaaa", List.of(Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of("aaabab", "bbbaaa", List.of(Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.PRESENT, Mark.CORRECT, Mark.ABSENT)),
                Arguments.of("aaaaaa", "bbbbbb", List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of("gehoor", "onmens", List.of(Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of("aabbcc", "abcabc", List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.CORRECT)),
                Arguments.of("alianna", "liniaal", List.of(Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of("heren", "haren", List.of(Mark.CORRECT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of("eeaaae", "aaeeae", List.of(Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT))
        );
    }
    
    
    @Test
    void isSolved_false_whenNotAllMarksCorrect() {
        Attempt attempt = new Attempt("cigaz", true);

        Feedback feedback = Feedback.createFeedback(attempt, "cigar");

        assertFalse(feedback.isSolved());
    }
}
