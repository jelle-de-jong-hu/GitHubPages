package nl.hu.s4.project.trainer.application;

import nl.hu.s4.project.trainer.domain.Feedback;
import nl.hu.s4.project.trainer.domain.GameStatus;

import java.util.List;

public record Progress(long gameId, int roundNumber, String solution, int attempts, GameStatus status, int score, List<Feedback> feedback, String[] hint) {

}
