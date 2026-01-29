package nl.hu.s4.project.lingo.domain;

import jakarta.persistence.*;
import nl.hu.s4.project.lingo.domain.exception.MaxAttemptsReachedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Round {

    private static final int MAX_ATTEMPTS = 5;
    @Id
    @GeneratedValue
    private long id;
    private String solution;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<Feedback> attempts;

    public Round(String solution) {
        this.solution = solution.toLowerCase();
        attempts = new ArrayList<>();
    }

    public Round() {

    }

    public long getId() {
        return id;
    }

    public String getSolution() {
        return solution;
    }

    List<Feedback> getAttempts() {
        return attempts;
    }

    void guess(Attempt attempt) {
        if (attempts.size() >= MAX_ATTEMPTS) {
            throw new MaxAttemptsReachedException();
        }

        attempts.add(Feedback.createFeedback(attempt, solution));
    }

    boolean isLost() {
        return attempts.size() >= MAX_ATTEMPTS && !isSolved();
    }

    boolean isSolved() {
        if (attempts.isEmpty()) {
            return false;
        }
        return attempts.getLast().isSolved();
    }

    int calculateScore() {
        if (!isSolved()) {
            return 0;
        }
       return 5 * (5 - attempts.size()) + 5;
    }

    String[] generateHint() {
        String[] hint = new String[solution.length()];
        Arrays.fill(hint, "_");
        hint[0] = String.valueOf(solution.charAt(0));

        for (Feedback feedback : attempts) {
            for (int i = 0; i < feedback.getMarks().size(); i++) {
                if (feedback.getMarks().get(i) == Mark.CORRECT) {
                    hint[i] = String.valueOf(solution.charAt(i));
                }
            }
        }
        return hint;
    }
}
