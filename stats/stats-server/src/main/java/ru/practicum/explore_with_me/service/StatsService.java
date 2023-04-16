package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.EndpointHitAPIDto;
import ru.practicum.explore_with_me.dto.EndpointHitOutDto;
import ru.practicum.explore_with_me.dto.ViewStatsOutDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    EndpointHitOutDto saveEndpointHit(EndpointHitAPIDto endpointHitAPIDto);

    List<ViewStatsOutDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
