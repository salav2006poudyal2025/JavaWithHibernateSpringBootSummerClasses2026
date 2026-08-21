package io.herald.MySpringWeb.Exception;

/**
 * Custom exception thrown when a requested user entity cannot be found in the database.
 * This is caught by the GlobalExceptionHandler to return a 404 Not Found response.
 */
public class UserNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
