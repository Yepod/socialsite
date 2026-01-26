package se.jensen.william.springboot.exceptions;

public class FriendshipNotFoundException extends RuntimeException {
    public FriendshipNotFoundException(Long friendshipId) {
        super("No such friendship...");
    }
}
