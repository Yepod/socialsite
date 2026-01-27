package se.jensen.william.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequestDTO(
        @NotBlank(message = "Text cannot be empty")
        @Size(max = 500, message = "Text cannot exceed 500 characters")
        String text
) {}