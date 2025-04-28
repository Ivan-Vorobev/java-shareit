package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    @Test
    void toEntity() {
        ItemDto itemDto = ItemDto
                .builder()
                .id(1L)
                .name("Name")
                .available(true)
                .description("Test description")
                .build();
        Item item = ItemMapper.toEntity(itemDto, null);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertNull(item.getOwner());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertNull(ItemMapper.toEntity(null, null));
        assertEquals(ItemMapper.toEntity(ItemDto.builder().requestId(1L).build(), null).getRequest().getId(), 1L);
    }

    @Test
    void toDto() {
        Item item = Item
                .builder()
                .name("Name")
                .available(true)
                .description("Test description")
                .build();
        ItemDto itemDto = ItemMapper.toDto(item);
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertNull(itemDto.getOwnerId());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertNull(ItemMapper.toDto((Item) null));
        assertEquals(ItemMapper.toDto(Item.builder().request(ItemRequest.builder().id(1L).build()).build()).getRequestId(), 1L);
    }

    @Test
    void testToDto() {
        Item item = Item
                .builder()
                .name("Name")
                .available(true)
                .description("Test description")
                .build();
        List<ItemDto> itemDtoList = ItemMapper.toDto(List.of(item));
        assertNotNull(itemDtoList);
        assertEquals(itemDtoList.get(0).getId(), item.getId());
        assertEquals(itemDtoList.get(0).getName(), item.getName());
        assertNull(itemDtoList.get(0).getOwnerId());
        assertEquals(itemDtoList.get(0).getAvailable(), item.getAvailable());
        assertEquals(itemDtoList.get(0).getDescription(), item.getDescription());
    }
}