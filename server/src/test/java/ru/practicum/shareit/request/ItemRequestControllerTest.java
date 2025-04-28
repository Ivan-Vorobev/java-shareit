package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ItemRequestController.class})
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void addRequest_shouldCreateItemRequestAndResponseHTTP200() throws Exception {
        Long userId = 10L;
        LocalDateTime date = LocalDateTime.now();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().description("Test description").build();
        ItemRequestDto createdItemRequestDto = ItemRequestDto
                .builder()
                .id(1L)
                .created(date)
                .description("Test description")
                .requestorId(userId)
                .build();

        Mockito.when(itemRequestService.add(itemRequestDto, userId)).thenReturn(createdItemRequestDto);

        mockMvc
                .perform(
                        post("/requests")
                                .content(mapper.writeValueAsString(itemRequestDto))
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdItemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(createdItemRequestDto.getDescription()))
                .andExpect(jsonPath("$.requestorId").value(createdItemRequestDto.getRequestorId()))
                .andExpect(jsonPath("$.created").value(createdItemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.items").value(createdItemRequestDto.getItems()));
    }

    @Test
    void findAllRequestorItems_shouldFindAllRequestorItemsAndResponseHTTP200() throws Exception {
        Long userId = 10L;
        LocalDateTime date = LocalDateTime.now();
        ItemRequestDto itemRequestDto1 = ItemRequestDto
                .builder()
                .id(1L)
                .created(date)
                .description("Test description1")
                .requestorId(userId)
                .build();
        ItemRequestDto itemRequestDto2 = ItemRequestDto
                .builder()
                .id(2L)
                .created(date)
                .description("Test description2")
                .requestorId(userId)
                .build();

        Mockito.when(itemRequestService.findAllRequestorItems(userId)).thenReturn(List.of(itemRequestDto1, itemRequestDto2));

        mockMvc
                .perform(
                        get("/requests")
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(itemRequestDto1.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto1.getDescription()))
                .andExpect(jsonPath("$[0].requestorId").value(itemRequestDto1.getRequestorId()))
                .andExpect(jsonPath("$[0].created").value(itemRequestDto1.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[0].items").isEmpty())
                .andExpect(jsonPath("$[1].id").value(itemRequestDto2.getId()))
                .andExpect(jsonPath("$[1].description").value(itemRequestDto2.getDescription()))
                .andExpect(jsonPath("$[1].requestorId").value(itemRequestDto2.getRequestorId()))
                .andExpect(jsonPath("$[1].created").value(itemRequestDto2.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[1].items").isEmpty());
    }

    @Test
    void findAllUserRequests_shouldFindAllUserRequestsItemsAndResponseHTTP200() throws Exception {
        Long userId = 10L;
        LocalDateTime date = LocalDateTime.now();
        ItemRequestDto itemRequestDto1 = ItemRequestDto
                .builder()
                .id(1L)
                .created(date)
                .description("Test description1")
                .requestorId(userId)
                .build();
        ItemRequestDto itemRequestDto2 = ItemRequestDto
                .builder()
                .id(2L)
                .created(date)
                .description("Test description2")
                .requestorId(userId)
                .build();
        ItemRequestDto itemRequestDto3 = ItemRequestDto
                .builder()
                .id(3L)
                .created(date)
                .description("Test description3")
                .requestorId(userId + 1)
                .build();

        Mockito.when(itemRequestService.findAllUserRequests(userId)).thenReturn(List.of(itemRequestDto1, itemRequestDto2, itemRequestDto3));

        mockMvc
                .perform(
                        get("/requests/all")
                                .header("X-Sharer-User-Id", userId)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(itemRequestDto1.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto1.getDescription()))
                .andExpect(jsonPath("$[0].requestorId").value(itemRequestDto1.getRequestorId()))
                .andExpect(jsonPath("$[0].created").value(itemRequestDto1.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[0].items").isEmpty())
                .andExpect(jsonPath("$[1].id").value(itemRequestDto2.getId()))
                .andExpect(jsonPath("$[1].description").value(itemRequestDto2.getDescription()))
                .andExpect(jsonPath("$[1].requestorId").value(itemRequestDto2.getRequestorId()))
                .andExpect(jsonPath("$[1].created").value(itemRequestDto2.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[1].items").isEmpty())
                .andExpect(jsonPath("$[2].id").value(itemRequestDto3.getId()))
                .andExpect(jsonPath("$[2].description").value(itemRequestDto3.getDescription()))
                .andExpect(jsonPath("$[2].requestorId").value(itemRequestDto3.getRequestorId()))
                .andExpect(jsonPath("$[2].created").value(itemRequestDto3.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[2].items").isEmpty());
    }

    @Test
    void findById_shouldFindByIdAndResponseHTTP200() throws Exception {
        Long searchId = 1L;
        LocalDateTime date = LocalDateTime.now();
        ItemRequestDto itemRequestDto = ItemRequestDto
                .builder()
                .id(searchId)
                .created(date)
                .description("Test description")
                .requestorId(10L)
                .build();

        Mockito.when(itemRequestService.findById(searchId)).thenReturn(itemRequestDto);

        mockMvc
                .perform(
                        get("/requests/" + searchId)
                                .header("X-Sharer-User-Id", 10L)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.requestorId").value(itemRequestDto.getRequestorId()))
                .andExpect(jsonPath("$.created").value(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.items").isEmpty());
    }
}