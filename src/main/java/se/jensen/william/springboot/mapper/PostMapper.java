package se.jensen.william.springboot.mapper;

import se.jensen.william.springboot.dto.PostRequestDTO;
import se.jensen.william.springboot.dto.PostResponseDTO;
import se.jensen.william.springboot.entities.Post;

public class PostMapper {

    // SKAPAR USER GENOM REQUESTDTO
    public static Post fromDto(PostRequestDTO postDto){
        Post post = new Post();
        setPostValues(post, postDto);
        return post;
    }

    // UPPDATERA EXISTERANDE USER
    public static Post fromDto(Post post, PostRequestDTO postDto) {
        setPostValues(post, postDto);
        return post;
    }

    // HJÄLP METOD FÖR ATT SÄTTA VÄRDEN
    private static void setPostValues(Post post, PostRequestDTO postDto){
        post.setText(postDto.text());
    }

    // SKICKAR USER TILL RESPONSEDTO
    public static PostResponseDTO toDto(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getUser().getId(),
                post.getText(),
                post.getCreatedAt()
        );
    }
}