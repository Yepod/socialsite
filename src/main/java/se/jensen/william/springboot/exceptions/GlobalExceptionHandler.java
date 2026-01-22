package se.jensen.william.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global undantagshanterare för applikationen
 *
 * klassen fångar upp och hanterar excpetioner från controllers i hela applikationen.
 * den undantagshanterar alla rest controllers.
 *
 * klassen hanterar tre undantag som är:
 * valderingsfel - retunerar 400 bad request
 * användare itne hittade - returnera 404 not found
 * användare finns redan  - returnerar 409 conflict
 *
 * @author William
 */


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors (MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for(org.springframework.validation.FieldError fieldError
        : ex.getBindingResult().getFieldErrors()) {

            String fieldName = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            errors.put(fieldName, message);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors); // ERROR CODE: 400 BAD REQUEST
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // ERROR CODE: 404 NOT FOUND
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handleUserAlreadyExist(UserAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage()); // ERROR CODE: 409 CONFLICT
    }
}
