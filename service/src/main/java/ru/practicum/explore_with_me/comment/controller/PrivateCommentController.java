package ru.practicum.explore_with_me.comment.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.comment.dto.InputCommentDto;
import ru.practicum.explore_with_me.comment.dto.OutputCommentDto;
import ru.practicum.explore_with_me.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrivateCommentController {
    final String pathVarUserId = "userId";
    final String pathVarCommentId = "commentId";
    final String pathVarEventId = "eventId";
    final String pathForCommentId = "/{commentId}";

    final CommentService commentService;

    @Autowired
    public PrivateCommentController(@Qualifier("CommentServiceDb") CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OutputCommentDto addComment(@Valid @RequestBody InputCommentDto inputCommentDto,
                                       @PathVariable(name = pathVarUserId) Long userId,
                                       @RequestParam(name = pathVarEventId) Long eventId) {
        return commentService.addComment(inputCommentDto, userId, eventId);
    }

    @PatchMapping(pathForCommentId)
    public OutputCommentDto updateComment(@Valid @RequestBody InputCommentDto inputCommentDto,
                                          @PathVariable(name = pathVarUserId) Long userId,
                                          @PathVariable(name = pathVarCommentId) Long commentId) {
        return commentService.updateComment(inputCommentDto, userId, commentId);
    }

    @DeleteMapping(pathForCommentId)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable(name = pathVarUserId) Long userId,
                              @PathVariable(name = pathVarCommentId) Long commentId) {
        commentService.deleteComment(userId, commentId);
    }

    @GetMapping
    public List<OutputCommentDto> getOwnComments(@PathVariable(name = pathVarUserId) Long userId) {
        return commentService.getOwnComments(userId);
    }
}
