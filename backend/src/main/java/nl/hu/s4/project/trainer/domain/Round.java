package nl.hu.s4.project.trainer.domain;

import java.util.ArrayList;
import java.util.List;

public class Round {

    private static final int MAX_ATTEMPTS = 5;

    private long id;
    private final String solution;
    private final List<Feedback> attempts;

    public Round(String solution) {
        this.solution = solution;
        attempts = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public List<Feedback> getAttempts() {
        return attempts;
    }

    public List<Feedback> guess(Attempt attempt) {
        if (attempts.size() >= MAX_ATTEMPTS) {
            throw new IllegalStateException("Maximum number of attempts reached");
        }

        attempts.add(Feedback.createFeedback(attempt, solution));
        return attempts;
    }
}
