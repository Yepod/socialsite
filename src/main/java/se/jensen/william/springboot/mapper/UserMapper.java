package se.jensen.william.springboot.mapper;

import org.springframework.stereotype.Component;
import se.jensen.william.springboot.dto.UserRequestDTO;
import se.jensen.william.springboot.dto.UserResponseDTO;
import se.jensen.william.springboot.entities.User;

/**
 * Mapper-klass för konvertering mellan User-entitet och DTO-objekt.
 *
 * Klassen handhåller metoder för att mappa mellan user entiteten och DTO.
 *
 * @author William
 */

@Component
public class UserMapper {

    // SKAPAR USER GENOM REQUESTDTO
    public User fromDto(UserRequestDTO userDto){
        User user = new User();
        setUserValues(user, userDto);
        return user;
    }

    // UPPDATERA EXISTERANDE USER
    public void fromDto(User user, UserRequestDTO userDto) {
        setUserValues(user,userDto);
    }

    // HJÄLP METOD FÖR ATT SÄTTA VÄRDEN
    private void setUserValues(User user, UserRequestDTO userDto){
        user.setEmail(userDto.email());
        user.setUsername(userDto.username());
        user.setPassword(userDto.password());
        user.setRole(userDto.role());
        user.setDisplayName(userDto.displayName());
        user.setBio(userDto.bio());
        user.setProfileImagePath(userDto.profileImagePath());
    }

    // SKICKAR USER TILL RESPONSEDTO
    public UserResponseDTO toDto(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole(),
                user.getDisplayName(),
                user.getBio(),
                user.getProfileImagePath()
        );
    }
}
