package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemRequestService itemRequestService;
    @Mock
    private UserService userService;

    @Test
    void add_shouldAddItemRequest() {
        Long itemRequestId = 10L;
        Long requestorId = 17L;
        LocalDateTime date = LocalDateTime.now();

        ItemRequest createdItemRequest = ItemRequest
                .builder()
                .id(itemRequestId)
                .requestor(User.builder().id(requestorId).build())
                .description("Test description")
                .created(date)
                .build();
        ItemRequestDto itemRequestDto = ItemRequestDto
                .builder()
                .description("Test description")
                .build();

        Mockito.when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(createdItemRequest);

        ItemRequestDto createdItemDRequest = itemRequestService.add(itemRequestDto, requestorId);

        Assertions.assertNotNull(createdItemDRequest);
        Assertions.assertEquals(createdItemDRequest.getId(), createdItemRequest.getId());
        Assertions.assertEquals(createdItemDRequest.getDescription(), createdItemRequest.getDescription());
        Assertions.assertEquals(createdItemDRequest.getRequestorId(), createdItemRequest.getRequestor().getId());
        Assertions.assertEquals(createdItemDRequest.getCreated(), createdItemRequest.getCreated());

        Mockito.verify(itemRequestRepository, Mockito.times(1)).save(any(ItemRequest.class));
        Mockito.verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void findById_shouldThrowError() {
        Long itemRequestId = 17L;

        Assertions.assertThrows(BadRequestException.class, () -> itemRequestService.findById(null));

        Assertions.assertThrows(NotFoundException.class, () -> itemRequestService.findById(itemRequestId));
    }

    @Test
    void findById_shouldFindById() {
        Long itemRequestId = 17L;
        LocalDateTime date = LocalDateTime.now();

        ItemRequest itemRequest = ItemRequest
                .builder()
                .id(itemRequestId)
                .requestor(User.builder().id(10L).build())
                .description("Test description")
                .created(date)
                .build();

        Mockito.when(itemRequestRepository.findById(itemRequestId)).thenReturn(Optional.of(itemRequest));

        ItemRequestDto itemRequestDto = itemRequestService.findById(itemRequestId);
        Assertions.assertEquals(itemRequestDto.getId(), itemRequest.getId());
        Assertions.assertEquals(itemRequestDto.getCreated(), itemRequest.getCreated());
        Assertions.assertEquals(itemRequestDto.getDescription(), itemRequest.getDescription());
        Assertions.assertEquals(itemRequestDto.getRequestorId(), itemRequest.getRequestor().getId());

        Mockito.verify(itemRequestRepository, Mockito.times(1)).findById(itemRequestId);
        Mockito.verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void findAllUserRequests_shouldFindAllUserRequests() {
        Long requestorId = 23L;
        Long itemRequestId = 17L;
        LocalDateTime date = LocalDateTime.now();

        ItemRequest itemRequest = ItemRequest
                .builder()
                .id(itemRequestId)
                .requestor(User.builder().id(requestorId).build())
                .description("Test description")
                .created(date)
                .build();

        User user = User.builder().id(requestorId).build();

        Mockito.when(userService.findById(requestorId)).thenReturn(user);
        Mockito.when(itemRequestRepository.findAllByRequestorIdIsNotOrderByCreatedDesc(user.getId())).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> itemRequestDtoList = itemRequestService.findAllUserRequests(requestorId);
        assertFalse(itemRequestDtoList.isEmpty());
        Assertions.assertEquals(itemRequestDtoList.size(), 1);

        Mockito.verify(itemRequestRepository, Mockito.times(1)).findAllByRequestorIdIsNotOrderByCreatedDesc(user.getId());
        Mockito.verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void findAllRequestorItems_shouldFindAllRequestorItems() {
        Long requestorId = 23L;
        Long itemRequestId = 17L;
        LocalDateTime date = LocalDateTime.now();

        ItemRequest itemRequest = ItemRequest
                .builder()
                .id(itemRequestId)
                .requestor(User.builder().id(requestorId).build())
                .description("Test description")
                .created(date)
                .build();

        User user = User.builder().id(requestorId).build();

        Mockito.when(userService.findById(requestorId)).thenReturn(user);
        Mockito.when(itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(user.getId())).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> itemRequestDtoList = itemRequestService.findAllRequestorItems(requestorId);
        assertFalse(itemRequestDtoList.isEmpty());
        Assertions.assertEquals(itemRequestDtoList.size(), 1);

        Mockito.verify(itemRequestRepository, Mockito.times(1)).findAllByRequestorIdOrderByCreatedDesc(user.getId());
        Mockito.verifyNoMoreInteractions(itemRequestRepository);
    }
}