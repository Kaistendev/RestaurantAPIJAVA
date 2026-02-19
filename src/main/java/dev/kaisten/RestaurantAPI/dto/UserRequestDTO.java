package dev.kaisten.RestaurantAPI.dto;

import dev.kaisten.RestaurantAPI.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "First name cannot be empty")
        @Size(max = 50, message = "First name cannot exceed 50 characters")
        String firstName,

        @NotBlank(message = "Last name cannot be empty")
        @Size(max = 50, message = "Last name cannot exceed 50 characters")
        String lastName,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotNull(message = "Role cannot be null")
        UserRole role
) {}
