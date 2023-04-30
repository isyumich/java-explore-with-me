package ru.practicum.explore_with_me.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.EndpointHitAPIDto;
import ru.practicum.explore_with_me.dto.EndpointHitOutDto;
import ru.practicum.explore_with_me.dto.ViewStatsOutDto;
import ru.practicum.explore_with_me.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsController {

    StatsService statsService;

    @Autowired
    public StatsController(@Qualifier("StatsServiceDb") StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitOutDto saveEndpointHit(@RequestBody @Valid EndpointHitAPIDto endpointHitAPIDto) {
        log.info("Поступил запрос на сохранение запроса");
        return statsService.saveEndpointHit(endpointHitAPIDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsOutDto> getStats(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "start") LocalDateTime start,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "end") LocalDateTime end,
                                          @RequestParam(required = false) List<String> uris,
                                          @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Поступил запрос на получение статистики");
        return statsService.getStats(start, end, uris, unique);
    }
}
