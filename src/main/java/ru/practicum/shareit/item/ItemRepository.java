package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i WHERE i.available = true AND (i.name ILIKE %:needle% OR i.description ILIKE %:needle%)")
    List<Item> findAllByText(@Param("needle") String needle);

    List<Item> findAllByOwnerId(Long ownerId);
}
