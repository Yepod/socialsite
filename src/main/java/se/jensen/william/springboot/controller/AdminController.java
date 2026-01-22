package se.jensen.william.springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.jensen.william.springboot.dto.UserResponseDTO;
import se.jensen.william.springboot.dto.UserWithPostsResponseDto;
import se.jensen.william.springboot.repository.UserRepository;
import se.jensen.william.springboot.service.PostService;
import se.jensen.william.springboot.service.UserService;

import java.util.List;

/**
 * REST-controller för administratörsrelaterade funktioner
 *
 * controller hanterar alla endpoints under admin
 * Alla endpoints förutom startsidan kräver ADMIN-roll för åtkomst
 *
 * @author William
 */


@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PostService postService;
    private final UserRepository userRepository;

    public AdminController(UserService userService, PostService postService, UserRepository userRepository) {
        this.userService = userService;
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAdminPage(){
        return "This is the Admin Page";
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ADMIN')")
    // FIND ALL USERS
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> userFromDb = userService.getAllUsers();
        return ResponseEntity.ok(userFromDb);   // 200 Ok
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    // FIND USER BY ID
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id)); // 200 Ok
    }

    @GetMapping("/{id}/with-posts")
    @PreAuthorize("hasRole('ADMIN')")
    // FIND USER WITH ALL POSTS
    public ResponseEntity<UserWithPostsResponseDto> getUserWithPosts(@PathVariable Long id){
        UserWithPostsResponseDto response = userService.getUserWithPosts(id);
        return ResponseEntity.ok(response);     // 200 Ok
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    // DELETE USER
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
