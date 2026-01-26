package se.jensen.william.springboot.exceptions;

public class FriendRequestToSelfException extends RuntimeException {
    public FriendRequestToSelfException(Long id) {
        super("Can't send friend-request to yourself...");
    }
}
