package io.herald.MySpringWeb.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * Centralized exception handler for REST controllers.
 * Intercepts thrown exceptions and translates them into appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles cases where a requested user is not found in the database.
     * @param ex The custom UserNotFoundException thrown.
     * @return 404 NOT FOUND with the error message.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles bean validation constraint violations (e.g. @NotBlank or @Email).
     * @param ex The ConstraintViolationException thrown.
     * @return 400 BAD REQUEST containing a list of validation errors.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        // Use Java Streams to extract and join all constraint error messages
        String errors = ex.getConstraintViolations().stream()
                .map(v -> v.getMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>("Validation Error: " + errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Fallback handler for all other unhandled exceptions.
     * @param ex The generic Exception caught.
     * @return 500 INTERNAL SERVER ERROR with the exception details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return new ResponseEntity<>("Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
