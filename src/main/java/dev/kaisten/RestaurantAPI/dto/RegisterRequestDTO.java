package dev.kaisten.RestaurantAPI.dto;

import dev.kaisten.RestaurantAPI.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "First name cannot be empty") String firstName,

        @NotBlank(message = "Last name cannot be empty") String lastName,

        @NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email format") String email,

        @NotBlank(message = "Password cannot be empty") @Size(min = 8, message = "Password must be at least 8 characters long") String password,

        // Optional, defaults to CLIENT in service/controller if null
        UserRole role) {
}
