package se.jensen.william.springboot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.jensen.william.springboot.dto.UserResponseDTO;
import se.jensen.william.springboot.dto.UserResponseDTOBuilder;
import se.jensen.william.springboot.entities.User;
import se.jensen.william.springboot.mapper.UserMapper;
import se.jensen.william.springboot.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void getAllUsersTest() {
        //Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("William");

        User user2 = new User();
        user.setId(2L);
        user.setUsername("Linus");

        List<User> users = List.of(user, user2);

        UserResponseDTO dto1 = UserResponseDTOBuilder.builder()
                .withId(1L)
                .withUsername("William")
                .build();

        UserResponseDTO dto2 = UserResponseDTOBuilder.builder()
                .withId(2L)
                .withUsername("Linus")
                .build();

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        // ACT
        List<UserResponseDTO> result = userService.getAllUsers();

        // ASSERT
        assertEquals(2, result.size());
    }
}
