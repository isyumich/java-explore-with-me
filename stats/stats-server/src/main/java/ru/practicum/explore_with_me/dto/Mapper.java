package ru.practicum.explore_with_me.dto;

import ru.practicum.explore_with_me.EndpointHitAPIDto;
import ru.practicum.explore_with_me.ViewStatsAPIDto;
import ru.practicum.explore_with_me.model.EndpointHit;
import ru.practicum.explore_with_me.model.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {
    public static EndpointHit endpointHitFromAPIDtoToModel(EndpointHitAPIDto endpointHitAPIDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return EndpointHit.builder()
                .app(endpointHitAPIDto.getApp())
                .ip(endpointHitAPIDto.getIp())
                .uri(endpointHitAPIDto.getUri())
                .timestamp(LocalDateTime.parse(endpointHitAPIDto.getTimestamp(), formatter))
                .build();
    }

    public static ViewStats viewStatsFromAPIDtoToModel(ViewStatsAPIDto viewStatsAPIDto) {
        return ViewStats.builder()
                .app(viewStatsAPIDto.getApp())
                .uri(viewStatsAPIDto.getUri())
                .hits(viewStatsAPIDto.getHits())
                .build();
    }

    public static EndpointHitOutDto endpointHitFromModelToOutDto(EndpointHit endpointHit) {
        return EndpointHitOutDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    public static ViewStatsOutDto viewStatsFromModelToOutDto(ViewStats viewStats) {
        return ViewStatsOutDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }
}
