package ru.practicum.explore_with_me.event.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.event.dto.InputEventDto;
import ru.practicum.explore_with_me.event.dto.OutputEventDto;
import ru.practicum.explore_with_me.event.service.EventService;
import ru.practicum.explore_with_me.request.dto.InputRequestsStatusDto;
import ru.practicum.explore_with_me.request.dto.OutputRequestDto;
import ru.practicum.explore_with_me.request.dto.RequestStatusUpdateResult;
import ru.practicum.explore_with_me.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrivateEventController {
    final String pathForEventId = "/{eventId}";
    final String pathForRequests = "/requests";
    final EventService eventService;
    final RequestService requestService;

    @Autowired
    public PrivateEventController(@Qualifier("EventServiceDb") EventService eventService,
                                  @Qualifier("RequestServiceDb") RequestService requestService) {
        this.eventService = eventService;
        this.requestService = requestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OutputEventDto addEvent(@Valid @RequestBody InputEventDto inputEventDto,
                                   @PathVariable(name = "userId") Long userId) {
        log.info("Запрос на создание нового события");
        return eventService.addEvent(inputEventDto, userId);
    }

    @PatchMapping(pathForEventId)
    public OutputEventDto updateEvent(@RequestBody InputEventDto inputEventDto,
                                      @PathVariable(name = "userId") Long userId,
                                      @PathVariable(name = "eventId") Long eventId) {
        log.info("Запрос на изменение события");
        return eventService.updateEvent(inputEventDto, userId, eventId);
    }

    @PatchMapping(pathForEventId + pathForRequests)
    public RequestStatusUpdateResult changeRequestStatus(@RequestBody InputRequestsStatusDto inputRequestsStatusDto,
                                                         @PathVariable(name = "userId") Long userId,
                                                         @PathVariable(name = "eventId") Long eventId) {
        log.info("Запрос на изменения статуса события");
        return requestService.changeRequestStatus(inputRequestsStatusDto, userId, eventId);
    }

    @GetMapping
    List<OutputEventDto> getEvents(@PathVariable(name = "userId") Long userId,
                                   @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                   @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Запрос на получение событий пользователя");
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping(pathForEventId)
    OutputEventDto getEvent(@PathVariable(name = "userId") Long userId,
                            @PathVariable(name = "eventId") Long eventId) {
        log.info("Запрос на получение события по id");
        return eventService.getEvent(userId, eventId);
    }

    @GetMapping(pathForEventId + pathForRequests)
    List<OutputRequestDto> getEventParticipants(@PathVariable(name = "userId") Long userId,
                                                @PathVariable(name = "eventId") Long eventId) {
        log.info("Запрос на получение заявки по событиям");
        return requestService.getEventParticipants(userId, eventId);
    }
}
