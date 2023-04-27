package ru.practicum.explore_with_me.request.dto;

import ru.practicum.explore_with_me.request.model.Request;

public class RequestDtoMapper {
    public static OutputRequestDto modelToOutputMapper(Request request) {
        return OutputRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }
}
