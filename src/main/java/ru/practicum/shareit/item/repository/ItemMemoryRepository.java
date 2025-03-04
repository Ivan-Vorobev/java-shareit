package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemMemoryRepository {
    private Long increment = 0L;

    private final Map<Long, Item> itemsData = new HashMap<>();
    private final Map<Long, List<Long>> userItemsData = new HashMap<>();

    public Item addItem(Item item) {
        item.setId(generateId());
        itemsData.put(item.getId(), item);
        List<Long> userItems = userItemsData.get(item.getOwner().getId());

        if (Objects.isNull(userItems)) {
            userItems = new ArrayList<>();
            userItemsData.put(item.getOwner().getId(), userItems);
        }

        userItems.add(item.getId());

        return item;
    }

    public Item getItemById(Long itemId) {
        Item item = itemsData.get(itemId);

        if (item == null) {
            throw new ItemNotFoundException("Item with id '" + itemId + "' not found");
        }

        return item;
    }

    public List<Item> getAll(Long ownerId) {
        return itemsData.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .toList();
    }

    public List<Item> searchAll(String needle) {
        return itemsData.values().stream()
                .filter(item -> item.getAvailable()
                                && !needle.isEmpty()
                                && (
                                item.getName().toLowerCase().contains(needle.toLowerCase())
                                        || item.getDescription().toLowerCase().contains(needle.toLowerCase())
                        )
                )
                .toList();
    }

    public Item updateItem(Item item) {
        Item searchItem = itemsData.get(item.getId());

        if (item.getName() != null) {
            searchItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            searchItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            searchItem.setAvailable(item.getAvailable());
        }

        return searchItem;
    }


    private Long generateId() {
        return ++increment;
    }
}
