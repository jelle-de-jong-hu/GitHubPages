package nl.hu.s4.project.trainer.domain;

import java.util.ArrayList;
import java.util.List;

public class Round {

    private long id;
    private final String solution;
    private final List<Feedback> attempts;

    public Round(String solution) {
        this.solution = solution;
        attempts = new ArrayList<>();
    }

    public String getSolution() {
        return solution;
    }

    public long getId() {
        return id;
    }

    public List<Feedback> getAttempts() {
        return attempts;
    }

    public List<Feedback> guess(Attempt attempt) {
        attempts.add(Feedback.createFeedback(attempt, solution));
        return attempts;
    }
}
