package nl.hu.s4.project.trainer.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Feedback {
    private Attempt attempt;
    private List<Mark> marks;

    Feedback() {

    }

    public static Feedback createFeedback(Attempt attempt, String solution) {
        Feedback feedback = new Feedback();
        feedback.attempt = attempt;
        feedback.generateMarks(solution, attempt);
        return feedback;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public Attempt getAttempt() {
        return attempt;
    }

    private void generateMarks(String solution, Attempt attempt) {
        var nrLetters = solution.length();

        if(!attempt.isValid()){
            marks = Collections.nCopies(nrLetters, Mark.INVALID);
        }

        var possiblyPresent = IntStream
                .range(0, nrLetters)
                .filter(i -> solution.charAt(i) != attempt.attempt().charAt(i))
                .mapToObj(solution::charAt)
                .collect(Collectors.toList());

        this.marks = IntStream.range(0, nrLetters).mapToObj(i -> {
            var solutionLetter = solution.charAt(i);
            var attemptLetter = attempt.attempt().charAt(i);

            return solutionLetter == attemptLetter
                    ? Mark.CORRECT
                    : possiblyPresent.remove((Character) attemptLetter) // Boxing needed to call correct overload
                    ? Mark.PRESENT
                    : Mark.ABSENT;
        }).toList();
    }

}
