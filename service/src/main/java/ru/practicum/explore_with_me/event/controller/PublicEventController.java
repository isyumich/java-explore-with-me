package ru.practicum.explore_with_me.event.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.constant.EventSortValue;
import ru.practicum.explore_with_me.event.dto.OutputEventDto;
import ru.practicum.explore_with_me.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicEventController {
    final EventService eventService;

    @Autowired
    public PublicEventController(@Qualifier("EventServiceDb") EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<OutputEventDto> getEvents(@RequestParam(name = "text", required = false) String text,
                                          @RequestParam(name = "categories", required = false) List<Long> categoryIds,
                                          @RequestParam(name = "paid", required = false) Boolean paid,
                                          @RequestParam(name = "rangeStart", required = false) String rangeStartString,
                                          @RequestParam(name = "rangeEnd", required = false) String rangeEndString,
                                          @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                          @RequestParam(name = "sort", required = false) EventSortValue eventSortValue,
                                          @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                          @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                          HttpServletRequest request) {
        return eventService.getEvents(text, categoryIds, paid, rangeStartString, rangeEndString, onlyAvailable, eventSortValue, from, size, request);
    }

    @GetMapping("/{id}")
    public OutputEventDto getEvent(@PathVariable(name = "id") Long eventId,
                                   HttpServletRequest request) {
        return eventService.getEvent(eventId, request);
    }
}
