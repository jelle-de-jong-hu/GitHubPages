package nl.hu.s4.project.trainer.domain;

import java.util.List;

public record Progress(long gameId, int roundNumber, int attempts, GameStatus status, List<Feedback> feedback) {

}
