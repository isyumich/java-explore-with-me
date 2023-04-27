package ru.practicum.explore_with_me.request.service;

import ru.practicum.explore_with_me.request.dto.InputRequestsStatusDto;
import ru.practicum.explore_with_me.request.dto.OutputRequestDto;
import ru.practicum.explore_with_me.request.dto.RequestStatusUpdateResult;

import java.util.List;

public interface RequestService {
    OutputRequestDto addParticipationRequest(Long eventId, Long userId);

    OutputRequestDto cancelRequest(Long userId, Long requestId);

    RequestStatusUpdateResult changeRequestStatus(InputRequestsStatusDto inputRequestsStatusDto, Long userId, Long eventId);

    List<OutputRequestDto> getUserRequests(Long userId);

    List<OutputRequestDto> getEventParticipants(Long userId, Long eventId);
}
