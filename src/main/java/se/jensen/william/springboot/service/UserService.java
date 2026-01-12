package se.jensen.william.springboot.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.jensen.william.springboot.dto.*;
import se.jensen.william.springboot.exceptions.UserAlreadyExistException;
import se.jensen.william.springboot.exceptions.UserNotFoundException;
import se.jensen.william.springboot.mapper.UserMapper;
import se.jensen.william.springboot.entities.User;
import se.jensen.william.springboot.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
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
        User user = userMapper.fromDto(userDto);

        // HASHAR LÖSENORDET
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // KOLLAR OM USER REDAN FINNS
        boolean exists = userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (exists){
            throw new UserAlreadyExistException(user.getUsername(), user.getEmail());
        }

        // HÄR SPARAR MAN USER I DB OCH SPARAR USERN INKLUSIVE GENERERAT ID FRÅN DB I savedUser
        User savedUser = userRepository.save(user);

        // RETURNERAR USER INKLUSIVE ID TILL RESPONSEDTO
        return userMapper.toDto(savedUser);
    }

    // HÄMTAR USERS FRÅN DB, LÄGGER I LISTA, SKICKAR TILLBAKA EN MAPPNING TILL RESPONSEDTO
    public List<UserResponseDTO> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDto).toList(); // Method Reference
    }

    // HÄMTAR EN USER VIA ID
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toDto(user);
    }

    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return userMapper.toDto(user);
    }

    public UserWithPostsResponseDto getUserWithPosts(Long id){
        User user = userRepository.findUserWithPosts(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        List<PostResponseDTO> posts = user.getPosts()
                .stream()
                .map(p -> new PostResponseDTO(
                        p.getId(),
                        p.getText(),
                        p.getCreatedAt()
                ))
                .toList();

        UserResponseDTO dto = userMapper.toDto(user);

        return new UserWithPostsResponseDto(dto, posts);
    }

    // UPPDATERAR EXISTERANDE USER
    public UserResponseDTO updateUser(Long id, UserRequestDTO userDto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userMapper.fromDto(user, userDto);

        if(userDto.password() != null && !userDto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDto.password()));
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    // TAR BORT USER FRÅN DATABASEN
    public void deleteUser(Long id){
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()){
            userRepository.deleteById(id);
        }
        else {
            throw new UserNotFoundException(id);
        }
    }
}
