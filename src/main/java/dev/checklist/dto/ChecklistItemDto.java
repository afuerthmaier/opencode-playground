package dev.checklist.dto;

import java.time.LocalDateTime;

public record ChecklistItemDto(
        Long id,
        String title,
        String description,
        boolean completed,
        LocalDateTime createdAt
) {
}
