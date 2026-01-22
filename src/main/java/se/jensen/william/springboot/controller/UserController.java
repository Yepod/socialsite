package se.jensen.william.springboot.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.jensen.william.springboot.dto.PostRequestDTO;
import se.jensen.william.springboot.dto.PostResponseDTO;
import se.jensen.william.springboot.dto.UserRequestDTO;
import se.jensen.william.springboot.dto.UserResponseDTO;
import se.jensen.william.springboot.repository.UserRepository;
import se.jensen.william.springboot.service.PostService;
import se.jensen.william.springboot.service.UserService;

/**
 * REST-controller för användarhantering
 *
 * controller hanterar endpoints för hantering av användare i systemet
 * controller hanterar registrering av nya användare.
 *
 * @author William
 */


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final UserRepository userRepository;

    public UserController(UserService userService, PostService postService, UserRepository userRepository) {
        this.userService = userService;
        this.postService = postService;
        this.userRepository = userRepository;
    }


    // ADD USER
    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO response = userService.addUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED)    // Code: 201
                .body(response);
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<PostResponseDTO> createPostForUser(
            @PathVariable Long userId, @Valid @RequestBody PostRequestDTO reqDto) {

        PostResponseDTO response = postService.createPost(userId, reqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);    // Code: 201
    }

    @GetMapping("/my-profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> getProfile(Authentication authentication) {
        String username = authentication.getName();
        UserResponseDTO response = userService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    // UPDATE USER
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto)); // 200 Ok
    }

    /**
     * Hämtar inlägg för användares wall med pagination.
     * 10 inlägg per sida, sorterat nyast först.
     * Linus
     */
    @GetMapping("/{userId}/posts")
    public ResponseEntity<Page<PostResponseDTO>> getUserWall(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostResponseDTO> wall = postService.getPostsByUserId(userId, pageable);
        return ResponseEntity.ok(wall);
    }
}
