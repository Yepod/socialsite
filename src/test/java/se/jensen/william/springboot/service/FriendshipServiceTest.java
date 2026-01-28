package se.jensen.william.springboot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.jensen.william.springboot.entities.Friendship;
import se.jensen.william.springboot.entities.User;
import se.jensen.william.springboot.mapper.FriendshipMapper;
import se.jensen.william.springboot.mapper.UserMapper;
import se.jensen.william.springboot.repository.FriendshipRepository;
import se.jensen.william.springboot.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FriendshipServiceTest {
    @InjectMocks
    private FriendshipService friendshipService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendshipRepository friendshipRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private FriendshipMapper friendshipMapper;

    @Test
    public void acceptFriendRequestTest() {
        // Arrange
        User requester = new User();
        requester.setId(1L);
        requester.setUsername("NallePuh");

        User receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("Nasse");

        Friendship friendship = new Friendship(
                requester,
                receiver,
                Friendship.FriendshipStatus.PENDING
        );
        friendship.setId(10L);

        when(friendshipRepository.findById(10L))
                .thenReturn(Optional.of(friendship));

        // Act
        Friendship result = friendshipService.acceptFriendRequest(10L);

        // Assert
        assertEquals(Friendship.FriendshipStatus.ACCEPTED, result.getStatus());
    }
}
