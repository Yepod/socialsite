package se.jensen.william.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.william.springboot.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
