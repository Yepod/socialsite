package se.jensen.william.springboot.exceptions;

/**
 * Undantagsklass som används när ett inlägg inte hittas i databasen
 *
 * @author William
 */


public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("No post with id: " + id + " was found in database.");
    }
}
