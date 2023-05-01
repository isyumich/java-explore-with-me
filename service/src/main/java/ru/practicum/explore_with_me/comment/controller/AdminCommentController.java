package ru.practicum.explore_with_me.comment.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.comment.dto.OutputCommentDto;
import ru.practicum.explore_with_me.comment.service.CommentService;
import ru.practicum.explore_with_me.constant.CommentStatus;

import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCommentController {
    final CommentService commentService;

    @Autowired
    public AdminCommentController(@Qualifier("CommentServiceDb") CommentService commentService) {
        this.commentService = commentService;
    }

    @PatchMapping("/{commentId}")
    public OutputCommentDto moderateComment(@PathVariable(name = "commentId") Long commentId,
                                            @RequestParam(name = "status") CommentStatus commentStatus) {
        return commentService.moderateComment(commentId, commentStatus);
    }

    @GetMapping
    public List<OutputCommentDto> getAllComments(@RequestParam(name = "from") Integer from,
                                                 @RequestParam(name = "size") Integer size) {
        return commentService.getAllComments(from, size);
    }
}
