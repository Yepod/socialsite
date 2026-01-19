package se.jensen.william.springboot.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.jensen.william.springboot.dto.PostResponseDTO;
import se.jensen.william.springboot.dto.UserRequestDTO;
import se.jensen.william.springboot.dto.UserResponseDTO;
import se.jensen.william.springboot.dto.UserWithPostsResponseDto;
import se.jensen.william.springboot.entities.User;
import se.jensen.william.springboot.exceptions.UserAlreadyExistException;
import se.jensen.william.springboot.exceptions.UserNotFoundException;
import se.jensen.william.springboot.mapper.PostMapper;
import se.jensen.william.springboot.mapper.UserMapper;
import se.jensen.william.springboot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // SKAPAR USER GENOM REQUESTDTO, SKICKAR TILLBAKA USER TILL RESPONSEDTO
    public UserResponseDTO addUser(UserRequestDTO userDto) {
        logger.info("Creating new user with username: {}", userDto.username());

        User user = userMapper.fromDto(userDto);

        // HASHAR LÖSENORDET
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // KOLLAR OM USER REDAN FINNS
        boolean exists = userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (exists){
            logger.warn("User already exists with username: {} or email: {}", user.getUsername(), user.getEmail());
            throw new UserAlreadyExistException(user.getUsername(), user.getEmail());
        }

        // HÄR SPARAR MAN USER I DB OCH SPARAR USERN INKLUSIVE GENERERAT ID FRÅN DB I savedUser
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with id: {}", savedUser.getId());

        // RETURNERAR USER INKLUSIVE ID TILL RESPONSEDTO
        return userMapper.toDto(savedUser);
    }

    // HÄMTAR USERS FRÅN DB, LÄGGER I LISTA, SKICKAR TILLBAKA EN MAPPNING TILL RESPONSEDTO
    public List<UserResponseDTO> getAllUsers(){
        logger.debug("Fetching all users");
        List<User> users = userRepository.findAll();
        logger.info("Fetched {} users", users.size());
        return users.stream().map(userMapper::toDto).toList();
    }

    // HÄMTAR EN USER VIA ID
    public UserResponseDTO getUserById(Long id) {
        logger.debug("Fetching user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new UserNotFoundException(id);
                });

        logger.info("Successfully fetched user: {}", user.getUsername());
        return userMapper.toDto(user);
    }

    public UserResponseDTO getUserByUsername(String username) {
        logger.debug("Fetching user with username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User not found with username: {}", username);
                    return new UserNotFoundException(username);
                });

        logger.info("Successfully fetched user with username: {}", username);
        return userMapper.toDto(user);
    }

    public UserWithPostsResponseDto getUserWithPosts(Long id){
        logger.debug("Fetching user with posts for id: {}", id);

        User user = userRepository.findUserWithPosts(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new UserNotFoundException(id);
                });

        /**
         *  Mappar användarens posts till PostResponseDTO med hjälp av PostMapper
         *  Linus
         */
        List<PostResponseDTO> posts = user.getPosts()
                .stream()
                .map(PostMapper::toDto)
                .toList();

        UserResponseDTO dto = userMapper.toDto(user);
        logger.info("Successfully fetched user with {} posts", posts.size());

        return new UserWithPostsResponseDto(dto, posts);
    }

    // UPPDATERAR EXISTERANDE USER
    public UserResponseDTO updateUser(Long id, UserRequestDTO userDto){
        logger.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cannot update - User not found with id: {}", id);
                    return new UserNotFoundException(id);
                });

        userMapper.fromDto(user, userDto);

        if (userDto.password() != null && !userDto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDto.password()));
        }

        User savedUser = userRepository.save(user);
        logger.info("User updated successfully with id: {}", savedUser.getId());

        return userMapper.toDto(savedUser);
    }

    // TAR BORT USER FRÅN DATABASEN
    public void deleteUser(Long id){
        logger.info("Attempting to delete user with id: {}", id);

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(id);
            logger.info("User deleted successfully with id: {}", id);
        }
        else {
            logger.warn("Cannot delete - User not found with id: {}", id);
            throw new UserNotFoundException(id);
        }
    }
}