package nl.hu.s4.project.trainer.domain;

import jakarta.persistence.Entity;



public record Attempt(String attempt, boolean isValid) {
}
