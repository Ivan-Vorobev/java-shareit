package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enumiration.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemService itemService;
    @Mock
    private UserService userService;
    @Mock
    private BookingService bookingService;

    @Test
    void addItem_shouldAddItem() {
        Long itemId = 1L;
        Long userId = 10L;
        ItemDto itemDto = ItemDto
                .builder()
                .name("Name")
                .ownerId(userId)
                .available(true)
                .description("Test description")
                .build();
        Item item = Item
                .builder()
                .name("Name")
                .owner(User.builder().id(userId).build())
                .available(true)
                .description("Test description")
                .build();

        Item createdItem = Item
                .builder()
                .id(itemId)
                .name("Name")
                .owner(User.builder().id(userId).build())
                .available(true)
                .description("Test description")
                .build();

        UserDto user = UserDto.builder().id(userId).build();

        Mockito.when(userService.getById(any())).thenReturn(user);
        Mockito.when(itemRepository.save(item)).thenReturn(createdItem);

        ItemDto createdItemDto = itemService.addItem(itemDto, userId);

        Assertions.assertNotNull(createdItemDto);
        Assertions.assertEquals(createdItemDto.getId(), createdItem.getId());
        Assertions.assertEquals(createdItemDto.getDescription(), createdItem.getDescription());
        Assertions.assertEquals(createdItemDto.getOwnerId(), createdItem.getOwner().getId());
        Assertions.assertEquals(createdItemDto.getName(), createdItem.getName());
        Assertions.assertEquals(createdItemDto.getAvailable(), createdItem.getAvailable());

        Mockito.verify(itemRepository, Mockito.times(1)).save(item);
        Mockito.verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void addComment_shouldThrowError() {
        Long itemId = 1L;
        Long userId = 10L;
        LocalDateTime date = LocalDateTime.now();
        BookingDto bookingDto = BookingDto
                .builder()
                .id(1L)
                .itemId(itemId + 1L)
                .start(date.minusDays(10L))
                .end(date.minusDays(5L))
                .build();
        CommentDto commentDto = CommentDto
                .builder()
                .text("Text")
                .authorId(userId)
                .authorName("Name")
                .build();

        User user = User.builder().id(userId).name("Name").build();

        Mockito.when(userService.findById(anyLong())).thenReturn(user);
        Mockito.when(bookingService.getAllBooking(anyLong(), any())).thenReturn(List.of(bookingDto));
        Assertions.assertThrows(BadRequestException.class, () -> itemService.addComment(commentDto, itemId, userId));
    }

    @Test
    void addComment_shouldAddComment() {
        Long commentId = 13L;
        Long itemId = 1L;
        Long userId = 10L;
        LocalDateTime date = LocalDateTime.now();
        BookingDto bookingDto = BookingDto
                .builder()
                .id(1L)
                .itemId(itemId)
                .start(date.minusDays(10L))
                .end(date.minusDays(5L))
                .build();
        CommentDto commentDto = CommentDto
                .builder()
                .text("Text")
                .authorId(userId)
                .authorName("Name")
                .build();
        Comment comment = Comment
                .builder()
                .id(commentId)
                .text("Text")
                .author(User.builder().id(userId).name("Name").build())
                .created(date)
                .build();

        User user = User.builder().id(userId).name("Name").build();

        Mockito.when(userService.findById(anyLong())).thenReturn(user);
        Mockito.when(bookingService.getAllBooking(anyLong(), any())).thenReturn(List.of(bookingDto));
        Mockito.when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto createdComment = itemService.addComment(commentDto, itemId, userId);

        Assertions.assertNotNull(createdComment);
        Assertions.assertEquals(createdComment.getId(), comment.getId());
        Assertions.assertEquals(createdComment.getText(), comment.getText());
        Assertions.assertEquals(createdComment.getAuthorId(), comment.getAuthor().getId());
        Assertions.assertEquals(createdComment.getAuthorName(), comment.getAuthor().getName());
        Assertions.assertEquals(createdComment.getCreated(), comment.getCreated());

        Mockito.verify(commentRepository, Mockito.times(1)).save(any(Comment.class));
        Mockito.verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void updateItem_shouldUpdateItem() {
        Long itemId = 1L;
        Long userId = 10L;
        ItemDto itemDto = ItemDto
                .builder()
                .name("Name")
                .ownerId(userId)
                .available(true)
                .description("Test description")
                .build();
        Item item = Item
                .builder()
                .id(itemId)
                .name("Name")
                .owner(User.builder().id(userId).build())
                .available(true)
                .description("Test description")
                .build();

        UserDto user = UserDto.builder().id(userId).name("Name").build();

        Mockito.when(userService.getById(any())).thenReturn(user);
        Mockito.when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto updatedItemDto = itemService.updateItem(itemId, itemDto, userId);

        Assertions.assertNotNull(updatedItemDto);
        Assertions.assertEquals(updatedItemDto.getId(), item.getId());
        Assertions.assertEquals(updatedItemDto.getDescription(), item.getDescription());
        Assertions.assertEquals(updatedItemDto.getOwnerId(), item.getOwner().getId());
        Assertions.assertEquals(updatedItemDto.getName(), item.getName());
        Assertions.assertEquals(updatedItemDto.getAvailable(), item.getAvailable());


        Mockito.verify(itemRepository, Mockito.times(1)).save(any(Item.class));
        Mockito.verify(itemRepository, Mockito.times(1)).findById(any());
        Mockito.verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void getItem_shouldGetItem() {
        Long itemId = 1L;
        Long userId = 10L;
        Item item = Item
                .builder()
                .id(itemId)
                .name("Name")
                .owner(User.builder().id(userId).build())
                .available(true)
                .description("Test description")
                .build();

        Mockito.when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        ItemDto updatedItemDto = itemService.getItem(itemId);

        Assertions.assertNotNull(updatedItemDto);
        Assertions.assertEquals(updatedItemDto.getId(), item.getId());
        Assertions.assertEquals(updatedItemDto.getDescription(), item.getDescription());
        Assertions.assertEquals(updatedItemDto.getOwnerId(), item.getOwner().getId());
        Assertions.assertEquals(updatedItemDto.getName(), item.getName());
        Assertions.assertEquals(updatedItemDto.getAvailable(), item.getAvailable());


        Mockito.verify(itemRepository, Mockito.times(1)).findById(any());
        Mockito.verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void getAllItem_shouldGetAllItem() {
        Long item1Id = 1L;
        Long item2Id = 2L;
        Long userId = 10L;
        Long bookingId = 5L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(2L);
        LocalDateTime endDate = LocalDateTime.now().plusDays(5L);
        User user = User.builder().id(userId).build();
        BookingDto booking1 = BookingDto
                .builder()
                .id(bookingId)
                .bookerId(user.getId())
                .itemId(item1Id)
                .item(ItemDto.builder().id(item1Id).name("Name").available(true).description("Desc").ownerId(userId).build())
                .start(startDate)
                .end(endDate)
                .status(BookingStatus.WAITING)
                .build();
        BookingDto booking2 = BookingDto
                .builder()
                .id(bookingId)
                .bookerId(user.getId())
                .itemId(item2Id)
                .item(ItemDto.builder().id(item2Id).name("Name").available(true).description("Desc").ownerId(userId).build())
                .start(startDate.minusDays(10L))
                .end(endDate)
                .status(BookingStatus.WAITING)
                .build();
        Item item1 = Item
                .builder()
                .id(item1Id)
                .name("Name")
                .owner(User.builder().id(userId).build())
                .available(true)
                .description("Test description")
                .build();
        Item item2 = Item
                .builder()
                .id(item2Id)
                .name("Name")
                .owner(User.builder().id(userId).build())
                .available(true)
                .description("Test description")
                .build();

        Mockito.when(itemRepository.findAllByOwnerId(userId)).thenReturn(List.of(item1, item2));
        Mockito.when(bookingService.getAllOwnerBooking(anyLong(), any())).thenReturn(List.of(booking1, booking2));

        List<ItemDto> updatedItems = itemService.getAllItem(userId);

        Assertions.assertNotNull(updatedItems);
        Assertions.assertEquals(updatedItems.get(0).getId(), item1.getId());
        Assertions.assertEquals(updatedItems.get(0).getDescription(), item1.getDescription());
        Assertions.assertEquals(updatedItems.get(0).getOwnerId(), item1.getOwner().getId());
        Assertions.assertEquals(updatedItems.get(0).getName(), item1.getName());
        Assertions.assertEquals(updatedItems.get(0).getAvailable(), item1.getAvailable());


        Mockito.verify(itemRepository, Mockito.times(1)).findAllByOwnerId(userId);
        Mockito.verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void getItemByText_shouldGetItemByText() {
        Assertions.assertTrue(itemService.getItemByText(null).isEmpty());
        Assertions.assertTrue(itemService.getItemByText("Text").isEmpty());
    }

    @Test
    void findItem_shouldThrowError() {
        Assertions.assertThrows(RuntimeException.class, () -> itemService.findItem(1L));
    }

    @Test
    void findItem_shouldfindItem() {
        Long itemId = 1L;
        Long userId = 10L;
        Item item = Item
                .builder()
                .id(itemId)
                .name("Name")
                .owner(User.builder().id(userId).build())
                .available(true)
                .description("Test description")
                .build();

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Item updatedItems = itemService.findItem(itemId);

        Assertions.assertNotNull(updatedItems);
        Assertions.assertEquals(updatedItems.getId(), item.getId());
        Assertions.assertEquals(updatedItems.getDescription(), item.getDescription());
        Assertions.assertEquals(updatedItems.getOwner().getId(), item.getOwner().getId());
        Assertions.assertEquals(updatedItems.getName(), item.getName());
        Assertions.assertEquals(updatedItems.getAvailable(), item.getAvailable());


        Mockito.verify(itemRepository, Mockito.times(1)).findById(itemId);
        Mockito.verifyNoMoreInteractions(commentRepository);
    }
}