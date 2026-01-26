package se.jensen.william.springboot.dto;

public record FriendshipRequestDTO(
        Long requesterId,
        Long addresseeId
) {
}
