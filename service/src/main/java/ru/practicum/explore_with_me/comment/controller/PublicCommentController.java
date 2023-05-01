package ru.practicum.explore_with_me.comment.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.comment.dto.OutputCommentDto;
import ru.practicum.explore_with_me.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicCommentController {
    final CommentService commentService;

    @Autowired
    public PublicCommentController(@Qualifier("CommentServiceDb") CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping()
    public List<OutputCommentDto> getCommentsByEventId(@RequestParam(name = "eventId") Long eventId,
                                                       @RequestParam(name = "from") Integer from,
                                                       @RequestParam(name = "size") Integer size) {
        return commentService.getCommentsByEventId(eventId, from, size);
    }

    @GetMapping("/{commentId}")
    public OutputCommentDto getComment(@PathVariable(name = "commentId") Long commentId) {
        return commentService.getComment(commentId);
    }
}
