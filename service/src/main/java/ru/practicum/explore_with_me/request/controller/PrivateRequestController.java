package ru.practicum.explore_with_me.request.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.request.dto.OutputRequestDto;
import ru.practicum.explore_with_me.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivateRequestController {
    String pathVarUserId = "userId";
    RequestService requestService;

    @Autowired
    public PrivateRequestController(@Qualifier("RequestServiceDb") RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OutputRequestDto addParticipationRequest(@RequestParam(name = "eventId", required = false) Long eventId,
                                                    @PathVariable(name = pathVarUserId) Long userId) {
        log.info(String.format("%s %d %s %d", "Запрос на создание заявки от пользователя с id =", userId, "для события с id =", eventId));
        return requestService.addParticipationRequest(eventId, userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public OutputRequestDto cancelRequest(@PathVariable(name = pathVarUserId) Long userId,
                                          @PathVariable(name = "requestId") Long requestId) {
        log.info(String.format("%s %d %s %d", "Запрос отмену заявки с id =", requestId, "от пользователя с id =", requestId));
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping
    public List<OutputRequestDto> getUserRequests(@PathVariable(name = pathVarUserId) Long userId) {
        log.info(String.format("%s %d", "Запрос на получение всех заявок от пользователя с id =", userId));
        return requestService.getUserRequests(userId);
    }
}
