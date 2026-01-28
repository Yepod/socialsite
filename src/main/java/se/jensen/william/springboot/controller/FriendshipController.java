package se.jensen.william.springboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.jensen.william.springboot.dto.FriendshipResponseDTO;
import se.jensen.william.springboot.dto.FriendshipRequestDTO;
import se.jensen.william.springboot.entities.Friendship;
import se.jensen.william.springboot.mapper.FriendshipMapper;
import se.jensen.william.springboot.service.FriendshipService;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendshipController {
    private final FriendshipService friendshipService;
    private final FriendshipMapper friendshipMapper;

    public FriendshipController(FriendshipService friendshipService, FriendshipMapper friendshipMapper) {
        this.friendshipService = friendshipService;
        this.friendshipMapper = friendshipMapper;
    }

    @PostMapping("/friend-request")
    public ResponseEntity<FriendshipResponseDTO> sendFriendRequest(
            @RequestBody FriendshipRequestDTO dto,
            Authentication authentication
    ) {
        // Säkerhetskontroll – blockerar felaktiga requesterId
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        FriendshipResponseDTO response =
                friendshipService.sendFriendRequest(
                        dto.requesterId(),
                        dto.addresseeId()
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<FriendshipResponseDTO> acceptFriendRequest(
            @PathVariable Long id
    ) {
        Friendship friendship = friendshipService.acceptFriendRequest(id);
        return ResponseEntity.ok(friendshipMapper.toDto(friendship));
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<FriendshipResponseDTO> declineFriendRequest(
            @PathVariable Long id
    ) {
        Friendship friendship = friendshipService.declineFriendRequest(id);
        return ResponseEntity.ok(friendshipMapper.toDto(friendship));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FriendshipResponseDTO>> getAllFriendshipStatus(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                friendshipService.getAllFriendshipStatus(userId)
        );
    }
}
