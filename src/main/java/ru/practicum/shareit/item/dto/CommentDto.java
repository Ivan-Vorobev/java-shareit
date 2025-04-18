package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;
    @NotNull
    private String text;
    private Long authorId;
    private String authorName;
    private Long itemId;
    private LocalDateTime created;
}
