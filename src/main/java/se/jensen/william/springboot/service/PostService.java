package se.jensen.william.springboot.service;

import org.springframework.stereotype.Service;
import se.jensen.william.springboot.dto.PostRequestDTO;
import se.jensen.william.springboot.dto.PostResponseDTO;
import se.jensen.william.springboot.exceptions.PostNotFoundException;
import se.jensen.william.springboot.exceptions.UserNotFoundException;
import se.jensen.william.springboot.mapper.PostMapper;
import se.jensen.william.springboot.entities.Post;
import se.jensen.william.springboot.entities.User;
import se.jensen.william.springboot.repository.PostRepository;
import se.jensen.william.springboot.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public PostResponseDTO createPost(Long userId, PostRequestDTO postDto){
        LocalDateTime now = LocalDateTime.now();

        Post post = new Post();
        post.setText(postDto.text());
        post.setCreatedAt(now);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        post.setUser(user);
        Post savedPost = postRepository.save(post);

        return PostMapper.toDto(savedPost);
    }

    public PostResponseDTO updatePost(Long id, PostRequestDTO postDto){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        post.setText(postDto.text());

        Post savedPost = postRepository.save(post);
        return PostMapper.toDto(savedPost);
    }

    public void deletePost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        postRepository.delete(post);
    }
}
