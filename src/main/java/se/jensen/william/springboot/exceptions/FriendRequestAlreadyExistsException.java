package se.jensen.william.springboot.exceptions;

public class FriendRequestAlreadyExsistsException extends RuntimeException {
    public FriendRequestAlreadyExsistsException(String message) {
        super(message);
    }
}
