package se.jensen.william.springboot.exceptions;

public class FriendRequestToSelf extends RuntimeException {
    public FriendRequestToSelf(String message) {
        super("Can't send friend-request to yourself...");
    }
}
