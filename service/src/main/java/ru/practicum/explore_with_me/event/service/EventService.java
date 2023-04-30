package ru.practicum.explore_with_me.event.service;

import ru.practicum.explore_with_me.constant.EventSortValue;
import ru.practicum.explore_with_me.constant.EventState;
import ru.practicum.explore_with_me.event.dto.InputEventDto;
import ru.practicum.explore_with_me.event.dto.OutputEventDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    OutputEventDto addEvent(InputEventDto inputEventDto, Long userId);

    OutputEventDto updateEvent(InputEventDto inputEventDto, Long userId, Long eventId);

    OutputEventDto updateEvent(InputEventDto inputEventDto, Long eventId);

    List<OutputEventDto> getEvents(Long userId, Integer from, Integer size);

    List<OutputEventDto> getEvents(List<Long> userIds, EventState states, List<Long> categoryIds, String rangeStartString,
                                   String rangeEndString, Integer from, Integer size);

    List<OutputEventDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStartString, String rangeEndString,
                                   Boolean onlyAvailable, EventSortValue eventSortValue, Integer from, Integer size, HttpServletRequest request);

    OutputEventDto getEvent(Long userId, Long eventId);

    OutputEventDto getEvent(Long eventId, HttpServletRequest request);
}
