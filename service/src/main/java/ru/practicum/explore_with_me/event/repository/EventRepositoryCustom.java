package ru.practicum.explore_with_me.event.repository;

import ru.practicum.explore_with_me.constant.EventParamsType;
import ru.practicum.explore_with_me.constant.EventSortValue;
import ru.practicum.explore_with_me.constant.EventState;
import ru.practicum.explore_with_me.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findEventsByParams(String text, List<Long> userIds, EventState states, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortValue eventSortValue,
                                   Integer from, Integer size, EventParamsType typeParams);
}
