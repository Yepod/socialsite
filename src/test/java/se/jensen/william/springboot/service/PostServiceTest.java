package se.jensen.william.springboot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;
import se.jensen.william.springboot.dto.PostRequestDTO;
import se.jensen.william.springboot.dto.PostResponseDTO;
import se.jensen.william.springboot.entities.Post;
import se.jensen.william.springboot.entities.User;
import se.jensen.william.springboot.repository.PostRepository;
import se.jensen.william.springboot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks
    PostService postService;
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;

    @Test
    public void createPostTest() {
        // Arrange

        // Skapar User
        User user = new User();
        user.setId(1L);
        user.setUsername("Håkan");

        // DTO som skickas in till createPost
        PostRequestDTO postRequestDTO =
                new PostRequestDTO("This is the perfect test text.");

        // Post som ska returneras när repository.save() anropas.
        Post savedPost = new Post();
        savedPost.setId(10L); // ID brukar sättas av Databasen.
        savedPost.setText(postRequestDTO.text());
        savedPost.setUser(user);
        savedPost.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(postRepository.save(any(Post.class)))
                .thenReturn(savedPost);

        // Act
        PostResponseDTO result =
                postService.createPost(1L, postRequestDTO);

        // Assert
        assertEquals("This is the perfect test text.", result.text());
    }
}
