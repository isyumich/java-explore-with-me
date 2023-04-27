package ru.practicum.explore_with_me.event.dto;

import ru.practicum.explore_with_me.event.model.Event;

public class EventDtoMapper {
    public static Event inputToModelMapper(InputEventDto inputEventDto) {
        return Event.builder()
                .annotation(inputEventDto.getAnnotation())
                .description(inputEventDto.getDescription())
                .eventDate(inputEventDto.getEventDate())
                .paid(inputEventDto.getPaid())
                .participantLimit(inputEventDto.getParticipantLimit())
                .requestModeration(inputEventDto.getRequestModeration())
                .title(inputEventDto.getTitle())
                .build();
    }

    public static OutputEventDto modelToOutputMapper(Event event) {
        return OutputEventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .initiator(event.getInitiator())
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }
}
