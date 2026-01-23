package se.jensen.william.springboot.dto;

public record SendFriendshipRequestDTO(
        Long requesterId,
        Long addresseeId
) {
}
