package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ItemController.class})
class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getAllItem_shouldGetAllItemAndResponseHTTP200() throws Exception {
        Long userId = 10L;
        ItemDto itemDto = ItemDto
                .builder()
                .id(1L)
                .name("Name")
                .ownerId(userId)
                .available(true)
                .description("Test description")
                .build();

        Mockito.when(itemService.getAllItem(userId)).thenReturn(List.of(itemDto));

        mockMvc
                .perform(
                        get("/items")
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].ownerId").value(itemDto.getOwnerId()))
                .andExpect(jsonPath("$[0].available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()));
    }

    @Test
    void addItem_shouldAddItemAndResponseHTTP200() throws Exception {
        Long userId = 10L;
        ItemDto itemDto = ItemDto
                .builder()
                .name("Name")
                .description("Test description")
                .available(true)
                .build();
        ItemDto createdItemDto = ItemDto
                .builder()
                .id(1L)
                .name("Name")
                .ownerId(userId)
                .available(true)
                .description("Test description")
                .build();

        Mockito.when(itemService.addItem(itemDto, userId)).thenReturn(createdItemDto);

        mockMvc
                .perform(
                        post("/items")
                                .content(mapper.writeValueAsString(itemDto))
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdItemDto.getId()))
                .andExpect(jsonPath("$.description").value(createdItemDto.getDescription()))
                .andExpect(jsonPath("$.ownerId").value(createdItemDto.getOwnerId()))
                .andExpect(jsonPath("$.available").value(createdItemDto.getAvailable()))
                .andExpect(jsonPath("$.description").value(createdItemDto.getDescription()));
    }

    @Test
    void updateItem_shouldUpdateItemAndResponseHTTP200() throws Exception {
        Long userId = 10L;
        Long itemId = 1L;
        ItemDto itemDto = ItemDto
                .builder()
                .name("Name")
                .description("Test description-2")
                .available(false)
                .build();
        ItemDto updatedItemDto = ItemDto
                .builder()
                .id(itemId)
                .name("Name")
                .ownerId(userId)
                .available(true)
                .description("Test description-2")
                .build();

        Mockito.when(itemService.updateItem(itemId, itemDto, userId)).thenReturn(updatedItemDto);

        mockMvc
                .perform(
                        patch("/items/" + itemId)
                                .content(mapper.writeValueAsString(itemDto))
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedItemDto.getId()))
                .andExpect(jsonPath("$.description").value(updatedItemDto.getDescription()))
                .andExpect(jsonPath("$.ownerId").value(updatedItemDto.getOwnerId()))
                .andExpect(jsonPath("$.available").value(updatedItemDto.getAvailable()))
                .andExpect(jsonPath("$.description").value(updatedItemDto.getDescription()));
    }

    @Test
    void getItem_shouldGetItemAndResponseHTTP200() throws Exception {
        Long userId = 10L;
        Long itemId = 1L;
        ItemDto itemDto = ItemDto
                .builder()
                .id(itemId)
                .name("Name")
                .ownerId(userId)
                .available(true)
                .description("Test description-2")
                .build();

        Mockito.when(itemService.getItem(itemId)).thenReturn(itemDto);

        mockMvc
                .perform(
                        get("/items/" + itemId)
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.ownerId").value(itemDto.getOwnerId()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()));
    }

    @Test
    void searchItem_shouldSearchItemAndResponseHTTP200() throws Exception {
        Long userId = 10L;
        Long itemId = 1L;
        ItemDto itemDto = ItemDto
                .builder()
                .id(itemId)
                .name("Name")
                .ownerId(userId)
                .available(true)
                .description("Test description-2")
                .build();

        Mockito.when(itemService.getItemByText("description")).thenReturn(List.of(itemDto));

        mockMvc
                .perform(
                        get("/items/search?text=description")
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].ownerId").value(itemDto.getOwnerId()))
                .andExpect(jsonPath("$[0].available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()));
    }

    @Test
    void addComment_shouldAddCommentAndResponseHTTP200() throws Exception {
        Long commentId = 15L;
        Long userId = 10L;
        Long itemId = 1L;
        LocalDateTime date = LocalDateTime.now();
        CommentDto commentDto = CommentDto
                .builder()
                .authorName("Name")
                .text("Test description-2")
                .build();
        CommentDto createdCommentDto = CommentDto
                .builder()
                .id(commentId)
                .created(date)
                .itemId(itemId)
                .authorId(userId)
                .authorName("Name")
                .text("Test description-2")
                .build();

        Mockito.when(itemService.addComment(commentDto, itemId, userId)).thenReturn(createdCommentDto);

        mockMvc
                .perform(
                        post("/items/" + itemId + "/comment")
                                .content(mapper.writeValueAsString(commentDto))
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdCommentDto.getId()))
                .andExpect(jsonPath("$.created").value(createdCommentDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.itemId").value(createdCommentDto.getItemId()))
                .andExpect(jsonPath("$.authorId").value(createdCommentDto.getAuthorId()))
                .andExpect(jsonPath("$.authorName").value(createdCommentDto.getAuthorName()))
                .andExpect(jsonPath("$.text").value(createdCommentDto.getText()));
    }
}