package se.jensen.william.springboot.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.jensen.william.springboot.entities.User;
import se.jensen.william.springboot.repository.UserRepository;

/**
 * Service-klass för att ladda användardetaljer vid autentisering
 *
 * Klassen implementarar spring security unsderdetails service.
 * som är ansvarande för att hämta användarens information från databasen
 * under själva autentiseringsproccen
 *
 * @author William
 */


@Service
public class WebUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public WebUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new MyUserDetails(user);
    }
}
