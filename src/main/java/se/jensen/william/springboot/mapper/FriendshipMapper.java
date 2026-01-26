package se.jensen.william.springboot.mapper;

import org.springframework.stereotype.Component;
import se.jensen.william.springboot.dto.FriendshipResponseDTO;
import se.jensen.william.springboot.entities.Friendship;

@Component
public class FriendshipMapper {

    public FriendshipResponseDTO toDto (Friendship friendship) {
        return new FriendshipResponseDTO(
            friendship.getId(),
            friendship.getRequester(),
            friendship.getAddressee(),
            friendship.getStatus(),
            friendship.getCreatedAt()
        );
    }
}
