package se.jensen.william.springboot.exceptions;

public class FriendRequestAlreadyExistsException extends RuntimeException {
    public FriendRequestAlreadyExistsException(String message) {
        super(message);
    }
}
