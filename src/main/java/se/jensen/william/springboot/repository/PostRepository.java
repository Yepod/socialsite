package se.jensen.william.springboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.william.springboot.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Hittar alla inlägg med pagination.
     *
     * @param pageable Information som sidnummer, storlek och sortering.
     * @return Inlägg och metadata.
     * Linus
     */
    Page<Post> findAll(Pageable pageable);

    /**
     * Hämtar en specifik användares inlägg med pagination.
     * Wall-sidan.
     * Linus
     */
    Page<Post> findByUserId(Long userId, Pageable pageable);
}
