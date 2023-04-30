package ru.practicum.explore_with_me.event.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.constant.EventState;
import ru.practicum.explore_with_me.event.dto.InputEventDto;
import ru.practicum.explore_with_me.event.dto.OutputEventDto;
import ru.practicum.explore_with_me.event.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminEventController {

    EventService eventService;

    @Autowired
    public AdminEventController(@Qualifier("EventServiceDb") EventService eventService) {
        this.eventService = eventService;
    }

    @PatchMapping("/{eventId}")
    public OutputEventDto updateEvent(@RequestBody InputEventDto inputEventDto,
                                      @PathVariable(name = "eventId") Long eventId) {
        return eventService.updateEvent(inputEventDto, eventId);

    }

    @GetMapping
    public List<OutputEventDto> getEvents(@RequestParam(name = "users", required = false) List<Long> userIds,
                                          @RequestParam(name = "states", required = false) EventState states,
                                          @RequestParam(name = "categories", required = false) List<Long> categoryIds,
                                          @RequestParam(name = "rangeStart", required = false) String rangeStartString,
                                          @RequestParam(name = "rangeEnd", required = false) String rangeEndString,
                                          @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                          @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return eventService.getEvents(userIds, states, categoryIds, rangeStartString, rangeEndString, from, size);
    }
}
