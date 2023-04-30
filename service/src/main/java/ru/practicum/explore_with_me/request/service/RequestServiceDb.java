package ru.practicum.explore_with_me.request.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.constant.EventState;
import ru.practicum.explore_with_me.constant.RequestStatus;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.event.repository.EventRepository;
import ru.practicum.explore_with_me.exception.BadRequestException;
import ru.practicum.explore_with_me.exception.ConflictException;
import ru.practicum.explore_with_me.exception.ForbiddenException;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.request.dto.InputRequestsStatusDto;
import ru.practicum.explore_with_me.request.dto.OutputRequestDto;
import ru.practicum.explore_with_me.request.dto.RequestDtoMapper;
import ru.practicum.explore_with_me.request.dto.RequestStatusUpdateResult;
import ru.practicum.explore_with_me.request.model.Request;
import ru.practicum.explore_with_me.request.repository.RequestRepository;
import ru.practicum.explore_with_me.user.model.User;
import ru.practicum.explore_with_me.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("RequestServiceDb")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestServiceDb implements RequestService {

    RequestRepository requestRepository;
    UserRepository userRepository;
    EventRepository eventRepository;


    @Autowired
    public RequestServiceDb(RequestRepository requestRepository,
                            UserRepository userRepository,
                            EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public OutputRequestDto addParticipationRequest(Long eventId, Long userId) {
        Request request = new Request();
        Event event = getEvent(eventId);
        if (requestRepository.getCountRequestsForEventAndUser(userId, eventId) > 0) {
            String message = String.format("%s %d %s %d", "Пользователь с id =", userId, "уже подавал заявку на событие с id =", eventId);
            log.info(message);
            throw new ConflictException(message);
        }
        if (event.getParticipantLimit() == null || event.getParticipantLimit() <= eventRepository.getCountConfirmedRequests(eventId)) {
            String message = "Превышено допустимое количество участников";
            log.info(message);
            throw new ConflictException(message);
        }
        if (getUser(userId).equals(event.getInitiator())) {
            String message = "Недопустимо подавать заявку на собственное событие";
            log.info(message);
            throw new ConflictException(message);
        }
        if (!EventState.PUBLISHED.equals(event.getState())) {
            String message = "Заявки можно подавать только на опубликованные события";
            log.info(message);
            throw new ConflictException(message);
        }
        request.setRequester(getUser(userId));
        request.setEvent(getEvent(eventId));
        request.setCreated(LocalDateTime.now());
        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        return RequestDtoMapper.modelToOutputMapper(requestRepository.save(request));
    }

    @Override
    public OutputRequestDto cancelRequest(Long userId, Long requestId) {
        if (requestRepository.findById(requestId).isEmpty()) {
            String message = String.format("%s %d %s", "Заявка с id =", userId, "не найдена");
            log.info(message);
            throw new NotFoundException(message);
        }
        Request request = requestRepository.findById(requestId).get();
        if (!request.getRequester().equals(getUser(userId))) {
            String message = "Только автор заявки может её отменять";
            log.info(message);
            throw new NotFoundException(message);
        }
        request.setStatus(RequestStatus.CANCELED);
        request.setId(requestId);
        return RequestDtoMapper.modelToOutputMapper(requestRepository.save(request));
    }

    @Override
    public RequestStatusUpdateResult changeRequestStatus(InputRequestsStatusDto inputRequestsStatusDto, Long userId, Long eventId) {
        Event event = getEvent(eventId);
        if (event.getParticipantLimit() == null || event.getParticipantLimit() <= eventRepository.getCountConfirmedRequests(eventId)) {
            String message = "Превышено допустимое количество участников";
            log.info(message);
            throw new ConflictException(message);
        }
        RequestStatusUpdateResult requestStatusUpdateResult = new RequestStatusUpdateResult();
        List<OutputRequestDto> resultRequests = new ArrayList<>();
        List<Request> requests = requestRepository.findRequestsByRequestIds(inputRequestsStatusDto.getRequestIds());
        for (Request request : requests) {
            if (request.getStatus().equals(RequestStatus.CONFIRMED) && inputRequestsStatusDto.getStatus().equals(RequestStatus.CANCELED)) {
                String message = "Недопустимо отменять уже принятые заявки";
                log.info(message);
                throw new ConflictException(message);
            }
            request.setStatus(inputRequestsStatusDto.getStatus());
            requestRepository.save(request);
            resultRequests.add(RequestDtoMapper.modelToOutputMapper(request));
        }
        switch (inputRequestsStatusDto.getStatus()) {
            case CONFIRMED:
                requestStatusUpdateResult.setConfirmedRequests(resultRequests);
                break;
            case REJECTED:
                requestStatusUpdateResult.setRejectedRequests(resultRequests);
                break;
            default:
                break;
        }
        return requestStatusUpdateResult;
    }

    @Override
    public List<OutputRequestDto> getUserRequests(Long userId) {
        return requestListToOutputRequestDtoList(requestRepository.findRequestByRequester(getUser(userId)));
    }

    @Override
    public List<OutputRequestDto> getEventParticipants(Long userId, Long eventId) {
        Event event = getEvent(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            String message = "Только инициатор события может просматривать заявки";
            log.info(message);
            throw new ForbiddenException(message);
        }
        return requestListToOutputRequestDtoList(requestRepository.findRequestsByEventAndRequester(getEvent(eventId)));
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
        if (eventId == null) {
            String message = "id события должен быть указан";
            log.info(message);
            throw new BadRequestException(message);
        }
        if (eventRepository.findById(eventId).isEmpty()) {
            String message = String.format("%s %d %s", "Событие с id =", eventId, "не найдено");
            log.info(message);
            throw new NotFoundException(message);
        }
        return eventRepository.findById(eventId).get();
    }

    private List<OutputRequestDto> requestListToOutputRequestDtoList(List<Request> requests) {
        List<OutputRequestDto> outputRequestDtoList = new ArrayList<>();
        for (Request request : requests) {
            outputRequestDtoList.add(RequestDtoMapper.modelToOutputMapper(request));
        }
        return outputRequestDtoList;
    }
}
