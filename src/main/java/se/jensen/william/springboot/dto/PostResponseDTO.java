package se.jensen.william.springboot.dto;

import java.time.LocalDateTime;

// Utdata

/**
 * Lägger till userId och username för att frontend ska kunna länka.
 * Linus
 */
public record PostResponseDTO(
        Long id,
        String text,
        LocalDateTime createdAt,
        Long userId,
        String username
) {
}
