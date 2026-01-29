package nl.hu.s4.project.lingo.domain;

import jakarta.persistence.*;
import nl.hu.s4.project.lingo.data.MarksConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Entity
public class Feedback {
    @Embedded
    private Attempt attempt;
    @Convert(converter = MarksConverter.class)
    private List<Mark> marks;
    @Id
    @GeneratedValue
    private Long id;

    private Feedback(Attempt attempt, List<Mark> marks) {
        this.attempt = attempt;
        this.marks = marks;
    }

    public Feedback() {

    }

    public static Feedback createFeedback(Attempt attempt, String solution) {
        var marks = generateMarks(solution, attempt);
        return new Feedback(attempt, marks);
    }

    public Long getId() {
        return id;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public Attempt getAttempt() {
        return attempt;
    }

    public boolean isSolved() {
        return marks.stream().allMatch(m -> m == Mark.CORRECT);
    }

    private static List<Mark> generateMarks(String solution, Attempt attempt) {
        var nrLetters = solution.length();

        String attemptString = attempt.attempt().toLowerCase();

        if(!attempt.isValid() || attemptString.length() != nrLetters) {
           return Collections.nCopies(nrLetters, Mark.INVALID);
        }

        var possiblyPresent = new ArrayList<Character>();
        for (int i = 0; i < nrLetters; i++) {
            var solutionLetter = solution.charAt(i);
            var attemptLetter = attemptString.charAt(i);

            if (solutionLetter != attemptLetter) {
                possiblyPresent.add(solutionLetter);
            }
        }

        return IntStream.range(0, nrLetters).mapToObj(i -> {
            var solutionLetter = solution.charAt(i);
            var attemptLetter = attemptString.charAt(i);

            if (solutionLetter == attemptLetter) {
                checkIfPossiblyPresentAndRemove(possiblyPresent, attemptLetter);
                return Mark.CORRECT;
            } else if (checkIfPossiblyPresentAndRemove(possiblyPresent, attemptLetter)) {
                return Mark.PRESENT;
            } else {
                return Mark.ABSENT;
            }
        }).toList();
    }

    private static boolean checkIfPossiblyPresentAndRemove(List<Character> possiblyPresent, char attemptLetter) {
        return possiblyPresent.remove((Character) attemptLetter);
    }
}
