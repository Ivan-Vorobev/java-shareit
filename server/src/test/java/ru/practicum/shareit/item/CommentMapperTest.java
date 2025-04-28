package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    @Test
    void toEntity() {
        CommentDto commentDto = CommentDto
                .builder()
                .text("Text")
                .authorName("Name")
                .build();
        Comment comment = CommentMapper.toEntity(commentDto);
        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getText(), commentDto.getText());
        assertNull(CommentMapper.toEntity(null));
    }

    @Test
    void toDto() {
        Comment comment = Comment
                .builder()
                .id(2L)
                .text("Text")
                .build();
        Comment comment2 = Comment
                .builder()
                .id(2L)
                .text("Text")
                .item(Item.builder().id(10L).build())
                .build();
        CommentDto commentDto = CommentMapper.toDto(comment);
        assertEquals(commentDto.getId(), comment.getId());
        assertEquals(commentDto.getText(), comment.getText());
        assertEquals(commentDto.getText(), comment.getText());
        assertNull(CommentMapper.toEntity(null));
        CommentDto commentDto2 = CommentMapper.toDto(comment2);
        assertEquals(commentDto2.getItemId(), comment2.getItem().getId());
    }

    @Test
    void testToDto() {
        Comment comment = Comment
                .builder()
                .id(2L)
                .text("Text")
                .build();
        Comment comment2 = Comment
                .builder()
                .id(2L)
                .text("Text")
                .item(Item.builder().id(10L).build())
                .build();
        List<CommentDto> comments = CommentMapper.toDto(List.of(comment, comment2));
        assertNotNull(comments);
        assertEquals(comments.size(), 2);
    }
}