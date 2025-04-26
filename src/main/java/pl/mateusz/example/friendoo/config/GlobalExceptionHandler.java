package pl.mateusz.example.friendoo.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
