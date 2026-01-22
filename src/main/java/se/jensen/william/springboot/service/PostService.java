package se.jensen.william.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.jensen.william.springboot.dto.PostRequestDTO;
import se.jensen.william.springboot.dto.PostResponseDTO;
import se.jensen.william.springboot.entities.Post;
import se.jensen.william.springboot.entities.User;
import se.jensen.william.springboot.exceptions.PostNotFoundException;
import se.jensen.william.springboot.exceptions.UserNotFoundException;
import se.jensen.william.springboot.mapper.PostMapper;
import se.jensen.william.springboot.repository.PostRepository;
import se.jensen.william.springboot.repository.UserRepository;

import java.time.LocalDateTime;
/**
 * Service-klass för hantering av inlägg posts.
 *
 * Klassen iunnhåler affärslogik för alla operationer relaterade till inläggen.
 * Den hanterar skapande, uppdatering, borttagning eller hämtning av inläggen från databasen.
 *
 * @author William
 * @author Fadime
 * @author Linus
 */


@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public PostResponseDTO createPost(Long userId, PostRequestDTO postDto) {
        logger.info("Creating post for user id: {}", userId);

        LocalDateTime now = LocalDateTime.now();

        Post post = new Post();
        post.setText(postDto.text());
        post.setCreatedAt(now);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Cannot create post - User not found with id: {}", userId);
                    return new UserNotFoundException(userId);
                });

        post.setUser(user);
        Post savedPost = postRepository.save(post);

        logger.info("Post created successfully with id: {}", savedPost.getId());

        return PostMapper.toDto(savedPost);
    }

    public PostResponseDTO updatePost(Long id, PostRequestDTO postDto) {
        logger.info("Updating post with id: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cannot update - Post not found with id: {}", id);
                    return new PostNotFoundException(id);
                });

        post.setText(postDto.text());

        Post savedPost = postRepository.save(post);
        logger.info("Post updated successfully with id: {}", savedPost.getId());

        return PostMapper.toDto(savedPost);
    }

    public void deletePost(Long id) {
        logger.info("Attempting to delete post with id: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cannot delete - Post not found with id: {}", id);
                    return new PostNotFoundException(id);
                });

        postRepository.delete(post);
        logger.info("Post deleted successfully with id: {}", id);
    }

    public PostResponseDTO getPostById(Long id) {
        logger.debug("Fetching post with id: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Post not found with id: {}", id);
                    return new PostNotFoundException(id);
                });

        logger.info("Successfully fetched post with id: {}", id);
        return PostMapper.toDto(post);
    }

    public Page<PostResponseDTO> getAllPosts(Pageable pageable) {
        logger.debug("Fetching posts: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<PostResponseDTO> result = postRepository.findAll(pageable)
                .map(PostMapper::toDto);

        logger.info("Fetched {} posts out of {} total",
                result.getNumberOfElements(), result.getTotalElements());

        return result;
    }

    /**
     * Hämtar inlägg för användaren (Wall) med pagination.
     * Mappar automatiskt Post-entiteter till PostResponseDTO.
     * Linus
     */
    public Page<PostResponseDTO> getPostsByUserId(Long userId, Pageable pageable) {
        logger.debug("Fetching posts for user {}: page={}, size={}",
                userId, pageable.getPageNumber(), pageable.getPageSize());

        Page<PostResponseDTO> result = postRepository.findByUserId(userId, pageable)
                .map(PostMapper::toDto);

        logger.info("Fetched {} posts for user {}", result.getNumberOfElements(), userId);
        return result;
    }
}