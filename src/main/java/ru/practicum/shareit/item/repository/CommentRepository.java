package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(
            "select new ru.practicum.shareit.item.dto.CommentDto(c.id, c.text, c.authorName, c.created, c.item.id)" +
                    " from Comment as c" +
                    " where c.id = ?1"
    )
    CommentDto findCommentDto(Long id);

    @Query(
            "select new ru.practicum.shareit.item.dto.CommentDto(c.id, c.text, c.authorName, c.created, c.item.id)" +
                    " from Comment as c" +
                    " where item in ?1"
    )
    Set<CommentDto> findCommentDtosByItems(Collection<Item> items);

    @Query(
            "select new ru.practicum.shareit.item.dto.CommentDto(c.id, c.text, c.authorName, c.created, c.item.id)" +
                    " from Comment as c" +
                    " where item in ?1"
    )
    Set<CommentDto> findCommentDtosByItem(Item item);
}
