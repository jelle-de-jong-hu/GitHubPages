package nl.hu.s4.project.security.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Registration(@NotBlank String username, @Size(min = 5) String password, @NotBlank String firstName,
                           @NotBlank String lastName) {
}

