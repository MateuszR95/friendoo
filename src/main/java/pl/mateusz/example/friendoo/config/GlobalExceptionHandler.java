package pl.mateusz.example.friendoo.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.mateusz.example.friendoo.exceptions.AccessDeniedException;
import pl.mateusz.example.friendoo.exceptions.PostCommentNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;

/**
 * Global exception handler for handling validation exceptions in the application.
 * This class uses Spring's @RestControllerAdvice to handle exceptions globally.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles validation exceptions thrown by the application.
   *
   * @param ex the MethodArgumentNotValidException thrown
   * @return a ResponseEntity containing a map of field errors
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> {
      errors.put(error.getField(), error.getDefaultMessage());
    });
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(PostCommentNotFoundException.class)
  public ResponseEntity<String> handleCommentNotFound(PostCommentNotFoundException ex) {
    return ResponseEntity.status(404).body(ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
    return ResponseEntity.status(403).body(ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage() != null ? ex.getMessage()
        : "Nieprawidłowe dane wejściowe.");
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity.status(404).body(ex.getMessage());
  }

}
