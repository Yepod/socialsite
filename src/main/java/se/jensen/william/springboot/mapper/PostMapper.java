package se.jensen.william.springboot.mapper;

import se.jensen.william.springboot.dto.PostRequestDTO;
import se.jensen.william.springboot.dto.PostResponseDTO;
import se.jensen.william.springboot.entities.Post;

/**
 * Mapper-klass för konvertering mellan Post-entitet och DTO-objekt.
 *
 * Klassen hanterar metoder för att mappa mellan Post-entitet och DTO.
 *
 * @author William
 * @author Linus
 */
public class PostMapper {

    // SKAPAR POST FRÅN REQUESTDTO
    public static Post fromDto(PostRequestDTO postDto) {
        Post post = new Post();
        setPostValues(post, postDto);
        return post;
    }

    // UPPDATERAR EXISTERANDE POST
    public static Post fromDto(Post post, PostRequestDTO postDto) {
        setPostValues(post, postDto);
        return post;
    }

    // HJÄLPMETOD FÖR ATT SÄTTA VÄRDEN
    private static void setPostValues(Post post, PostRequestDTO postDto) {
        post.setText(postDto.text());
    }

    // KONVERTERAR POST → RESPONSEDTO
    public static PostResponseDTO toDto(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getText(),
                post.getCreatedAt(),
                post.getUser() != null ? post.getUser().getId() : null,
                post.getUser() != null ? post.getUser().getUsername() : "Anonym"
        );
    }
}
