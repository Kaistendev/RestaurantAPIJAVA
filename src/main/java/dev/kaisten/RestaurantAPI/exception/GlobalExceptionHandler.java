package dev.kaisten.RestaurantAPI.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
                        MethodArgumentNotValidException ex, WebRequest request) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                                .apiPath(request.getDescription(false))
                                .httpStatus(HttpStatus.BAD_REQUEST)
                                .errorReason("Validation Error")
                                .message("One or more fields failed validation.")
                                .validationErrors(errors)
                                .errorTime(LocalDateTime.now())
                                .build();

                return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
                        ResourceNotFoundException ex, WebRequest request) {
                ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                                .apiPath(request.getDescription(false))
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .errorReason("Resource Not Found")
                                .message(ex.getMessage())
                                .errorTime(LocalDateTime.now())
                                .build();
                return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(
                        DataIntegrityViolationException ex, WebRequest request) {
                String message = ex.getMostSpecificCause().getMessage();
                String userFriendlyMessage = "Database integrity violation.";

                if (message.contains("duplicate key")) {
                        userFriendlyMessage = "A record with the same unique value already exists.";
                } else if (message.contains("violates foreign key constraint")) {
                        userFriendlyMessage = "Operation cannot be performed as it violates data relationships.";
                }

                ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                                .apiPath(request.getDescription(false))
                                .httpStatus(HttpStatus.CONFLICT)
                                .errorReason("Data Integrity Violation")
                                .message(userFriendlyMessage)
                                .errorTime(LocalDateTime.now())
                                .build();
                return new ResponseEntity<>(apiErrorResponse, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handleGlobalException(
                        Exception ex, WebRequest request) {
                ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                                .apiPath(request.getDescription(false))
                                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                                .errorReason("Internal Server Error")
                                .message(ex.getMessage())
                                .errorTime(LocalDateTime.now())
                                .build();

                return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
