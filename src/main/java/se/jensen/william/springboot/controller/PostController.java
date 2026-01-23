package se.jensen.william.springboot.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.william.springboot.dto.PostRequestDTO;
import se.jensen.william.springboot.dto.PostResponseDTO;
import se.jensen.william.springboot.service.PostService;
import se.jensen.william.springboot.service.UserService;

/**
 * REST-controller för hantering av inlägg posts
 *
 * controller har endpoints för CRUD-operationer.
 * Controller hanterar nya inlägg som är skapade till specifika användare.
 *
 * @author William
 */


@RestController
@RequestMapping("/posts")
public class PostController {

    private final UserService userService;
    private final PostService postService;

    public PostController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    // CREATE NEW POST
    @PostMapping
    public ResponseEntity<PostResponseDTO> newPost(@Valid @RequestBody Long userId, PostRequestDTO dto) {

        PostResponseDTO response = postService.createPost(userId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // UPDATE POST
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePostById(@PathVariable Long id, @Valid @RequestBody PostRequestDTO dto) {
        return ResponseEntity.ok(postService.updatePost(id, dto)); // 200 ok
    }

    // DELETE POST
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDTO> feed = postService.getAllPosts(pageable);
        return ResponseEntity.ok(feed);
    }
}

