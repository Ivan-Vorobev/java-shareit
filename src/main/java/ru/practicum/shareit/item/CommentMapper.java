package ru.practicum.shareit.item;


import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class CommentMapper {
    public static Comment toEntity(CommentDto dto) {
        if (dto == null) {
            return null;
        }

        return Comment.builder()
                .id(dto.getId())
                .text(dto.getText())
                .created(dto.getCreated())
                .item(dto.getItemId() != null
                        ? Item.builder().id(dto.getItemId()).build()
                        : null
                )
                .author(dto.getAuthorId() != null
                        ? User.builder().id(dto.getAuthorId()).name(dto.getAuthorName()).build()
                        : null
                )
                .build();
    }

    public static CommentDto toDto(Comment model) {
        if (model == null) {
            return null;
        }

        return CommentDto.builder()
                .id(model.getId())
                .text(model.getText())
                .created(model.getCreated())
                .itemId(model.getItem() != null
                        ? model.getItem().getId()
                        : null
                )
                .authorId(model.getAuthor() != null
                        ? model.getAuthor().getId()
                        : null
                ).authorName(model.getAuthor() != null
                        ? model.getAuthor().getName()
                        : null
                )
                .build();
    }

    public static List<CommentDto> toDto(List<Comment> models) {
        if (models == null) {
            return List.of();
        }

        return models.stream()
                .map(CommentMapper::toDto)
                .toList();
    }
}
