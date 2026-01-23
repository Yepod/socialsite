package se.jensen.william.springboot.exceptions;

/**
 * Undantagsklass som användas när en användare med samma användarnamn eller e-post redan existerar.
 *
 * @author William
 */



public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String username, String email) {
        super("User with this Username: " + username + " or this Email: " + email + " already exists.");
    }
}
