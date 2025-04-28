package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ImageRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByText() {
        User user1 = User.builder().name("Name1").email("Email1@test.com").build();
        User user2 = User.builder().name("Name2").email("Email2@test.com").build();

        Item item1 = Item.builder().name("Name-1").available(true).owner(user1).description("TestXXX description-1").build();
        Item item2 = Item.builder().name("Name-2").available(true).owner(user2).description("Test description-2").build();

        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        itemRepository.save(item2);

        List<Item> resultData = itemRepository.findAllByText("TestXXX");

        assertNotNull(resultData);
        assertEquals(resultData.size(), 1);
        assertEquals(resultData.get(0), item1);
    }

    @Test
    void findAllByOwnerId() {
        User user1 = User.builder().name("Name1").email("Email1@test.com").build();
        User user2 = User.builder().name("Name2").email("Email2@test.com").build();

        Item item1 = Item.builder().name("Name-1").available(true).owner(user1).description("TestXXX description-1").build();
        Item item2 = Item.builder().name("Name-2").available(true).owner(user2).description("Test description-2").build();

        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        itemRepository.save(item2);

        List<Item> resultData = itemRepository.findAllByOwnerId(user2.getId());

        assertNotNull(resultData);
        assertEquals(resultData.size(), 1);
        assertEquals(resultData.get(0), item2);
    }
}
