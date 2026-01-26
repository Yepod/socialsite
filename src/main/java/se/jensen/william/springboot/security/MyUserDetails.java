package se.jensen.william.springboot.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.jensen.william.springboot.entities.User;

import java.util.Collection;
import java.util.List;
/**
 * En Anpassad implementering av UserDetails för Spring Security-autentisering
 *
 * klassen används som en adapter mellan spring security och user entitet.
 * klassen konventerar användarens roll till en åtkomst roll med en prefic "ROLE"
 * som ger åtkomstkontroll.
 *
 * @author William
 * @author Patric
 */


public class MyUserDetails implements UserDetails {

    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }
    public Long getId() {
        return user.getId();
    }


    public User getDomainUser(){
        return user;
    }
}
