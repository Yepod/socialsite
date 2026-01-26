package se.jensen.william.springboot.exceptions;

public class InvalidFriendshipStateException extends RuntimeException {
    public InvalidFriendshipStateException(String message) {
        super(message);
    }
}
