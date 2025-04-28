package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ImageRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByRequestorIdIsNotOrderByCreatedDesc() {
        User user1 = User.builder().name("Name1").email("Email1@test.com").build();
        User user2 = User.builder().name("Name2").email("Email2@test.com").build();
        LocalDateTime date = LocalDateTime.now();

        ItemRequest itemRequest1 = ItemRequest.builder().requestor(user1).description("Test description-1").created(date).build();
        ItemRequest itemRequest2 = ItemRequest.builder().requestor(user2).description("Test description-2").created(date).build();

        userRepository.save(user1);
        userRepository.save(user2);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);

        List<ItemRequest> resultData = itemRequestRepository.findAllByRequestorIdIsNotOrderByCreatedDesc(user1.getId());

        assertNotNull(resultData);
        assertEquals(resultData.size(), 1);
        assertEquals(resultData.get(0), itemRequest2);
    }

    @Test
    void findAllByRequestorIdOrderByCreatedDesc() {
        User user1 = User.builder().name("Name1").email("Email1@test.com").build();
        User user2 = User.builder().name("Name2").email("Email2@test.com").build();
        LocalDateTime date = LocalDateTime.now();

        ItemRequest itemRequest1 = ItemRequest.builder().requestor(user1).description("Test description-1").created(date).build();
        ItemRequest itemRequest2 = ItemRequest.builder().requestor(user2).description("Test description-2").created(date).build();

        userRepository.save(user1);
        userRepository.save(user2);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);

        List<ItemRequest> resultData = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(user1.getId());

        assertNotNull(resultData);
        assertEquals(resultData.size(), 1);
        assertEquals(resultData.get(0), itemRequest1);
    }
}
