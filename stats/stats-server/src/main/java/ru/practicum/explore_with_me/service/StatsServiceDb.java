package ru.practicum.explore_with_me.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.EndpointHitAPIDto;
import ru.practicum.explore_with_me.dto.EndpointHitOutDto;
import ru.practicum.explore_with_me.dto.Mapper;
import ru.practicum.explore_with_me.dto.ViewStatsOutDto;
import ru.practicum.explore_with_me.model.ViewStats;
import ru.practicum.explore_with_me.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@Qualifier("StatsServiceDb")
public class StatsServiceDb implements StatsService {
    final StatsRepository statsRepository;

    @Autowired
    public StatsServiceDb(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public EndpointHitOutDto saveEndpointHit(EndpointHitAPIDto endpointHitAPIDto) {
        return Mapper.endpointHitFromModelToOutDto(statsRepository.save(Mapper.endpointHitFromAPIDtoToModel((endpointHitAPIDto))));
    }

    @Override
    public List<ViewStatsOutDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris != null) {
            return unique ? viewStatFromModelToOutDto(statsRepository.getStatsByUniqIp(uris, start, end))
                    : viewStatFromModelToOutDto(statsRepository.getStats(uris, start, end));
        } else {
            return unique ? viewStatFromModelToOutDto(statsRepository.getStatsByUniqIp(start, end))
                    : viewStatFromModelToOutDto(statsRepository.getStats(start, end));
        }
    }

    private List<ViewStatsOutDto> viewStatFromModelToOutDto(List<ViewStats> viewStatsList) {
        List<ViewStatsOutDto> viewStatsOutDtoList = new ArrayList<>();
        for (ViewStats stats : viewStatsList) {
            viewStatsOutDtoList.add(Mapper.viewStatsFromModelToOutDto(stats));
        }
        return viewStatsOutDtoList;
    }
}
