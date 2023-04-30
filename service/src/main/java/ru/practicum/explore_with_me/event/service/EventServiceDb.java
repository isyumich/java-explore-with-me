package ru.practicum.explore_with_me.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.StatsClient;
import ru.practicum.explore_with_me.category.model.Category;
import ru.practicum.explore_with_me.category.repository.CategoryRepository;
import ru.practicum.explore_with_me.constant.*;
import ru.practicum.explore_with_me.event.dto.EventDtoMapper;
import ru.practicum.explore_with_me.event.dto.InputEventDto;
import ru.practicum.explore_with_me.event.dto.OutputEventDto;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.event.model.Location;
import ru.practicum.explore_with_me.event.repository.EventRepository;
import ru.practicum.explore_with_me.event.repository.LocationRepository;
import ru.practicum.explore_with_me.exception.ConflictException;
import ru.practicum.explore_with_me.exception.ForbiddenException;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.user.model.User;
import ru.practicum.explore_with_me.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("EventServiceDb")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventServiceDb implements EventService {
    StatsClient statsClient;
    EventRepository eventRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    LocationRepository locationRepository;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override//private
    public OutputEventDto addEvent(InputEventDto inputEventDto, Long userId) {
        if (inputEventDto.getEventDate().isBefore(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
            String message = "Дата начала события должна быть как минимум на два часа больше, чем текущая дата";
            log.info(message);
            throw new ConflictException(message);
        }
        if (userId == null) {
            String message = "Id пользователя должен быть указан при заведении заявки";
            log.info(message);
            throw new ConflictException(message);
        }
        Event event = EventDtoMapper.inputToModelMapper(inputEventDto);
        Location location = locationRepository.save(Location.builder()
                .lat(inputEventDto.getLocation().getLat())
                .lon(inputEventDto.getLocation().getLon())
                .build());
        event.setCategory(getCategory(inputEventDto.getCategory()));
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(getUser(userId));
        event.setLocation(location);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return modelToOutputDto(eventRepository.save(event));
    }

    @Override//private
    public OutputEventDto updateEvent(InputEventDto inputEventDto, Long userId, Long eventId) {
        Event event = getEventModel(eventId);
        if (event.getEventDate().isBefore(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
            String message = "Дата начала события должна быть как минимум на два часа больше, чем текущая дата";
            log.info(message);
            throw new ForbiddenException(message);
        }
        User user = getUser(userId);
        if (!event.getInitiator().equals(user)) {
            String message = "Только создатель может редактировать событие";
            log.info(message);
            throw new ForbiddenException(message);
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            String message = "Недопустимо редактировать опубликованное событие";
            log.info(message);
            throw new ConflictException(message);
        }
        Event updatedEvent = setUpdatedFields(inputEventDto, event);
        updatedEvent.setId(eventId);
        return modelToOutputDto(eventRepository.save(updatedEvent));
    }

    @Override//admin
    public OutputEventDto updateEvent(InputEventDto inputEventDto, Long eventId) {
        Event event = getEventModel(eventId);
        if (!event.getState().equals(EventState.PENDING) && inputEventDto.getStateAction().equals(EventChangeStatus.PUBLISH_EVENT)) {
            String message = "Нельзя опубликовать событие, не ожидающее публикации";
            log.info(message);
            throw new ConflictException(message);
        }
        if (event.getState().equals(EventState.PUBLISHED) && inputEventDto.getStateAction().equals(EventChangeStatus.REJECT_EVENT)) {
            String message = "Нельзя отменить опубликованное событие";
            log.info(message);
            throw new ConflictException(message);
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
            String message = "Дата начала события должна быть как минимум на два часа больше, чем текущая дата";
            log.info(message);
            throw new ForbiddenException(message);
        }
        Event updatedEvent = setUpdatedFields(inputEventDto, event);
        updatedEvent.setId(eventId);
        return modelToOutputDto(eventRepository.save(updatedEvent));
    }

    @Override//private
    public List<OutputEventDto> getEvents(Long userId, Integer from, Integer size) {
        User user = getUser(userId);
        return eventListToOutputEventDtoList(eventRepository.findEventsByInitiator(user, PageRequest.of(from / size, size)));
    }

    @Override//admin
    public List<OutputEventDto> getEvents(List<Long> userIds, EventState states, List<Long> categoryIds, String rangeStartString,
                                          String rangeEndString, Integer from, Integer size) {
        LocalDateTime rangeStart = rangeStartString != null ? LocalDateTime.parse(rangeStartString, dateTimeFormatter) : null;
        LocalDateTime rangeEnd = rangeEndString != null ? LocalDateTime.parse(rangeEndString, dateTimeFormatter) : null;
        return eventListToOutputEventDtoList(eventRepository.findEventsByParams(null, userIds, states, categoryIds,
                null, rangeStart, rangeEnd, null, null, from, size, EventParamsType.ADMIN));
    }

    @Override//public
    public List<OutputEventDto> getEvents(String text, List<Long> categoryIds, Boolean paid, String rangeStartString, String rangeEndString,
                                          Boolean onlyAvailable, EventSortValue eventSortValue, Integer from, Integer size, HttpServletRequest request) {
        LocalDateTime rangeStart = rangeStartString != null ? LocalDateTime.parse(rangeStartString, dateTimeFormatter) : null;
        LocalDateTime rangeEnd = rangeEndString != null ? LocalDateTime.parse(rangeEndString, dateTimeFormatter) : null;
        List<Event> events = eventRepository.findEventsByParams(text, null, null,
                categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, eventSortValue, from, size, EventParamsType.PUBLIC);
        sendEventStats(events, request, EventStats.EVENTS);
        List<OutputEventDto> outputEventDtoList = eventListToOutputEventDtoList(events);
        if (EventSortValue.VIEWS.equals(eventSortValue)) {
            outputEventDtoList = outputEventDtoList.stream().sorted(Comparator.comparing(OutputEventDto::getViews)).collect(Collectors.toList());
        }
        return outputEventDtoList;
    }

    @Override//private
    public OutputEventDto getEvent(Long userId, Long eventId) {
        return modelToOutputDto(getEventModel(eventId));
    }

    @Override//public
    public OutputEventDto getEvent(Long eventId, HttpServletRequest request) {
        Event event = getEventModel(eventId);
        sendEventStats(List.of(event), request, EventStats.EVENT);
        return modelToOutputDto(event);
    }

    private Category getCategory(Long categoryId) {
        if (categoryRepository.findById(categoryId).isEmpty()) {
            String message = String.format("%s %d %s", "Категория с id =", categoryId, "не найдена");
            log.info(message);
            throw new NotFoundException(message);
        }
        return categoryRepository.findById(categoryId).get();
    }

    private User getUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            String message = String.format("%s %d %s", "Пользователь с id =", userId, "не найден");
            log.info(message);
            throw new NotFoundException(message);
        }
        return userRepository.findById(userId).get();
    }

    private Event getEventModel(Long eventId) {
        if (eventRepository.findById(eventId).isEmpty()) {
            String message = String.format("%s %d %s", "Событие с id =", eventId, "не найдено");
            log.info(message);
            throw new NotFoundException(message);
        }
        return eventRepository.findById(eventId).get();
    }

    private OutputEventDto modelToOutputDto(Event event) {
        OutputEventDto outputEventDto = EventDtoMapper.modelToOutputMapper(event);
        outputEventDto.setViews(eventRepository.getCountViews(outputEventDto.getId()));
        outputEventDto.setConfirmedRequests(eventRepository.getCountConfirmedRequests(outputEventDto.getId()));
        return outputEventDto;
    }

    private List<OutputEventDto> eventListToOutputEventDtoList(List<Event> events) {
        List<OutputEventDto> outputEventDtoList = new ArrayList<>();
        for (Event event : events) {
            outputEventDtoList.add(modelToOutputDto(event));
        }
        return outputEventDtoList;
    }

    private Event setUpdatedFields(InputEventDto inputEventDto, Event event) {
        Event updatedEvent = EventDtoMapper.inputToModelMapper(inputEventDto);
        if (updatedEvent.getEventDate() == null) {
            updatedEvent.setEventDate(event.getEventDate());
        }
        if (LocalDateTime.now().isAfter(updatedEvent.getEventDate())) {
            String message = "Дата события не может быть меньше текущей даты";
            log.info(message);
            throw new ConflictException(message);
        }
        if (updatedEvent.getAnnotation() == null) {
            updatedEvent.setAnnotation(event.getAnnotation());
        }
        if (updatedEvent.getCategory() == null) {
            updatedEvent.setCategory(event.getCategory());
        }
        if (updatedEvent.getDescription() == null) {
            updatedEvent.setDescription(event.getDescription());
        }
        if (updatedEvent.getInitiator() == null) {
            updatedEvent.setInitiator(event.getInitiator());
        }
        if (updatedEvent.getLocation() == null) {
            updatedEvent.setLocation(event.getLocation());
        } else {
            Location location = locationRepository.save(Location.builder()
                    .lat(inputEventDto.getLocation().getLat())
                    .lon(inputEventDto.getLocation().getLon())
                    .build());
            updatedEvent.setLocation(location);
        }
        if (updatedEvent.getPaid() == null) {
            updatedEvent.setPaid(event.getPaid());
        }
        if (updatedEvent.getParticipantLimit() == null) {
            updatedEvent.setParticipantLimit(event.getParticipantLimit());
        }
        if (updatedEvent.getRequestModeration() == null) {
            updatedEvent.setRequestModeration(event.getRequestModeration());
        }
        if (updatedEvent.getState() == null) {
            updatedEvent.setState(event.getState());
        }
        if (updatedEvent.getTitle() == null) {
            updatedEvent.setTitle(event.getTitle());
        }
        updatedEvent.setCreatedOn(event.getCreatedOn());
        updatedEvent.setPublishedOn(event.getPublishedOn());
        if (inputEventDto.getStateAction() != null) {
            switch (inputEventDto.getStateAction()) {
                case REJECT_EVENT:
                case CANCEL_REVIEW:
                    updatedEvent.setState(EventState.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    updatedEvent.setState(EventState.PUBLISHED);
                    break;
                case SEND_TO_REVIEW:
                    updatedEvent.setState(EventState.PENDING);
                    break;
                default:
                    break;
            }
        }
        return updatedEvent;
    }

    private void sendEventStats(List<Event> events, HttpServletRequest request, EventStats typeStats) {
        switch (typeStats) {
            case EVENTS:
                statsClient.saveEndpointHit("service", "/events", request.getRemoteAddr(), LocalDateTime.now());
                break;
            case EVENT:
                statsClient.saveEndpointHit("service", "/events/" + events.get(0).getId(), request.getRemoteAddr(), LocalDateTime.now());
                break;
            default:
                break;
        }
    }
}
