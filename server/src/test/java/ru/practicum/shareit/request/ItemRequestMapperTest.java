package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    @Test
    void toEntity() {
        assertNull(ItemRequestMapper.toEntity(null));
        assertNull(ItemRequestMapper.toEntity(ItemRequestDto.builder().id(1L).build()).getRequestor());
    }

    @Test
    void toDto() {
        assertNull(ItemRequestMapper.toDto((ItemRequest) null));
    }

    @Test
    void testToDto() {
        assertEquals(ItemRequestMapper.toDto((List<ItemRequest>) null), List.of());
    }
}