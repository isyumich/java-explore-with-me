package ru.practicum.explore_with_me.comment.service;

import ru.practicum.explore_with_me.comment.dto.InputCommentDto;
import ru.practicum.explore_with_me.comment.dto.OutputCommentDto;
import ru.practicum.explore_with_me.constant.CommentStatus;

import java.util.List;

public interface CommentService {
    /*admin*/
    OutputCommentDto moderateComment(Long commentId, CommentStatus commentStatus);

    List<OutputCommentDto> getAllComments(Integer from, Integer size);

    /*private*/
    OutputCommentDto addComment(InputCommentDto inputCommentDto, Long userId, Long eventId);

    OutputCommentDto updateComment(InputCommentDto inputCommentDto, Long userId, Long commentId);

    void deleteComment(Long userId, Long commentId);

    List<OutputCommentDto> getOwnComments(Long userId);

    /*public*/
    List<OutputCommentDto> getCommentsByEventId(Long eventId, Integer from, Integer size);

    OutputCommentDto getComment(Long commentId);
}
