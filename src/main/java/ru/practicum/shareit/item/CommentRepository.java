package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {
}
