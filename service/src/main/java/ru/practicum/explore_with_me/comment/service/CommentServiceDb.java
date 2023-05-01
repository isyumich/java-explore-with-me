package ru.practicum.explore_with_me.comment.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.comment.dto.CommentDtoMapper;
import ru.practicum.explore_with_me.comment.dto.InputCommentDto;
import ru.practicum.explore_with_me.comment.dto.OutputCommentDto;
import ru.practicum.explore_with_me.comment.model.Comment;
import ru.practicum.explore_with_me.comment.repository.CommentRepository;
import ru.practicum.explore_with_me.constant.CommentStatus;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.event.repository.EventRepository;
import ru.practicum.explore_with_me.exception.ForbiddenException;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.user.model.User;
import ru.practicum.explore_with_me.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("CommentServiceDb")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceDb implements CommentService {
    CommentRepository commentRepository;
    EventRepository eventRepository;
    UserRepository userRepository;

    /*admin*/
    @Override
    public OutputCommentDto moderateComment(Long commentId, CommentStatus commentStatus) {
        Comment comment = getCommentModel(commentId);
        comment.setStatus(commentStatus);
        comment.setPublishedOn(LocalDateTime.now());
        return CommentDtoMapper.modelToOutputMapper(comment);
    }

    @Override
    public List<OutputCommentDto> getAllComments(Integer from, Integer size) {
        return FromModelListToOutDtoList(commentRepository.findAllComments(PageRequest.of(from / size, size)));
    }


    /*private*/
    @Override
    public OutputCommentDto addComment(InputCommentDto inputCommentDto, Long userId, Long eventId) {
        Comment comment = CommentDtoMapper.inputToModelMapper(inputCommentDto);
        comment.setAuthor(getUser(userId));
        comment.setEvent(getEvent(eventId));
        comment.setStatus(CommentStatus.PENDING);
        comment.setCreatedOn(LocalDateTime.now());
        return CommentDtoMapper.modelToOutputMapper(commentRepository.save(comment));
    }

    @Override
    public OutputCommentDto updateComment(InputCommentDto inputCommentDto, Long userId, Long commentId) {
        Comment comment = getCommentModel(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            String message = "Только автор комментария может его редактировать";
            log.info(message);
            throw new ForbiddenException(message);
        }
        comment.setText(inputCommentDto.getText());
        return CommentDtoMapper.modelToOutputMapper(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = getCommentModel(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            String message = "Только автор комментария может его удалить";
            log.info(message);
            throw new ForbiddenException(message);
        }
        commentRepository.delete(comment);
    }

    @Override
    public List<OutputCommentDto> getOwnComments(Long userId) {
        User author = getUser(userId);
        return FromModelListToOutDtoList(commentRepository.findCommentsByAuthor(author));
    }

    /*public*/
    @Override
    public List<OutputCommentDto> getCommentsByEventId(Long eventId, Integer from, Integer size) {
        Event event = getEvent(eventId);
        return FromModelListToOutDtoList(commentRepository.findCommentsByEvent(event, PageRequest.of(from / size, size)));
    }

    @Override
    public OutputCommentDto getComment(Long commentId) {
        return CommentDtoMapper.modelToOutputMapper(getCommentModel(commentId));
    }


    private User getUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            String message = String.format("%s %d %s", "Пользователь с id =", userId, "не найден");
            log.info(message);
            throw new NotFoundException(message);
        }
        return userRepository.findById(userId).get();
    }

    private Event getEvent(Long eventId) {
        if (eventRepository.findById(eventId).isEmpty()) {
            String message = String.format("%s %d %s", "Событие с id =", eventId, "не найдено");
            log.info(message);
            throw new NotFoundException(message);
        }
        return eventRepository.findById(eventId).get();
    }

    private Comment getCommentModel(Long commentId) {
        if (commentRepository.findById(commentId).isEmpty()) {
            String message = String.format("%s %d %s", "Комментарий с id =", commentId, "не найден");
            log.info(message);
            throw new NotFoundException(message);
        }
        return commentRepository.findById(commentId).get();
    }

    private List<OutputCommentDto> FromModelListToOutDtoList(List<Comment> comments) {
        List<OutputCommentDto> outputCommentDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            outputCommentDtoList.add(CommentDtoMapper.modelToOutputMapper(comment));
        }
        return outputCommentDtoList;
    }
}
