package se.jensen.william.springboot.entities;

import jakarta.persistence.*;

import java.util.List;

/**
 * Entitetsklass som representerar en användare i systemet
 *
 * KLasses moddelerar användar konton och all användarrelaterade information som är lagrad i systemet
 * bland annat e-post, användarnamn, lössenord, användar behörighet, profil information såsom, vissningsnamn
 *
 * Klassen har en One-to-Many-relation till Post-entiteten
 *
 * @author William
 */


@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String bio;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // LAZY IS DEFAULT
    private List<Post> posts;

    public User(){

    }

    public User(Long id, String email, String username, String password,
                String role, String displayName, String bio, String profileImagePath) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.displayName = displayName;
        this.bio = bio;
        this.profileImagePath = profileImagePath;
    }

    public Long getId() {return id;}
    public String getEmail() {return email;}
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public String getRole() {return role;}
    public String getDisplayName() {return displayName;}
    public String getBio() {return bio;}
    public String getProfileImagePath() {return profileImagePath;}
    public List<Post> getPosts() {return posts;}

    public void setId(Long id) {this.id = id;}
    public void setEmail(String email) {this.email = email;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setRole(String role) {this.role = role;}
    public void setDisplayName(String displayName) {this.displayName = displayName;}
    public void setBio(String bio) {this.bio = bio;}
    public void setProfileImagePath(String profileImagePath) {this.profileImagePath = profileImagePath;}

}
