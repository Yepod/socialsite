package se.jensen.william.springboot.dto;

import java.time.LocalDateTime;

// Utdata
public record PostResponseDTO(Long id, Long userId, String text, LocalDateTime createdAt) {
}