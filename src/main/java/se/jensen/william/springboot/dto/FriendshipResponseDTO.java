package se.jensen.william.springboot.dto;

import se.jensen.william.springboot.entities.Friendship;
import se.jensen.william.springboot.entities.User;

import java.time.Instant;

public record FriendRequestResponseDTO(
        Long id,
        User requester,
        User addressee,
        Friendship.FriendshipStatus status,
        Instant createdAt
) {
}
