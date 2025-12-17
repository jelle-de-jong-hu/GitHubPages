package nl.hu.s4.project.trainer.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Feedback {
    private String attempt;
    private List<Mark> marks;

    Feedback() {

    }

    public static Feedback createFeedback(String attempt, String solution) {
        Feedback feedback = new Feedback();
        feedback.attempt = attempt;

        return feedback;
    }

    private List<Mark> generateMarks(String solution, String attempt) {
        var marks = Collections.emptyList();
        var nrLetters = solution.length();

        if(!attempt.isValid()){
            marks = Collections.nCopies(nrLetters, Mark.INVALID);
            return;
        }

        var possiblyPresent = IntStream
                .range(0, nrLetters)
                .filter(i -> solution.charAt(i) != attempt.charAt(i))
                .mapToObj(solution::charAt)
                .collect(Collectors.toList());

        this.feedback = IntStream.range(0, nrLetters).mapToObj(i -> {
            var solutionLetter = solution.charAt(i);
            var attemptLetter = attempt.charAt(i);

            return solutionLetter == attemptLetter
                    ? Mark.CORRECT
                    : possiblyPresent.remove((Character) attemptLetter) // Boxing needed to call correct overload
                    ? Mark.PRESENT
                    : Mark.ABSENT;
        }).toList();
    }

}
