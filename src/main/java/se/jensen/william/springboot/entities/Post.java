package se.jensen.william.springboot.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


/**
 * Entitetsklass som representerar ett inlägg post i systemet
 *
 * Klassen modellerar användarinlägg och lagrar textinhållet.
 * och den tidstämplar när inlägget är skappad och senaste uppdaterad.
 *
 * @author William
 */


@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post(){}

    public Post(Long id, String text, LocalDateTime createdAt) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Long getId() {return id;}
    public String getText() {return text;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public User getUser() {return user;}

    public void setId(Long id) {this.id = id;}
    public void setText(String text) {this.text = text;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
    public void setUser(User user) {this.user = user;}
}
