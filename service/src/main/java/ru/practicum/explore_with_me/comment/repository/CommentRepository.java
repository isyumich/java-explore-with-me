package ru.practicum.explore_with_me.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.comment.model.Comment;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.user.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select c from Comment c where c.author = :author")
    List<Comment> findCommentsByAuthor(@Param("author") User author);

    @Query(value = "select c from Comment c where c.event = :event")
    List<Comment> findCommentsByEvent(@Param("event") Event event, Pageable pageable);

    @Query(value = "select c from Comment c")
    List<Comment> findAllComments(Pageable pageable);
}
