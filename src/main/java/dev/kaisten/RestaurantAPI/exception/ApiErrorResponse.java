package dev.kaisten.RestaurantAPI.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ApiErrorResponse(
                String apiPath,
                HttpStatus httpStatus,
                String errorReason,
                String message,
                LocalDateTime errorTime,
                Map<String, String> validationErrors) {
}
