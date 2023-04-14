package ru.practicum.explore_with_me;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsClient {
    final String url = "http://stats-server:9090";
    final RestTemplate restTemplate;

    public EndpointHitAPIDto saveEndpointHit(EndpointHitAPIDto endpointHitAPIDto) {
        HttpHeaders headers = restTemplate.headForHeaders(url);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EndpointHitAPIDto> request = new HttpEntity<>(endpointHitAPIDto, headers);
        restTemplate.exchange(url + "/hit", HttpMethod.POST, request, EndpointHitAPIDto.class);
        return endpointHitAPIDto;
    }

    public List<ViewStatsAPIDto> getStats(String start, String end, List<String> uris, Boolean unique) {

        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique);

        ResponseEntity<String> response = restTemplate.getForEntity(
                url + "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                String.class,
                parameters);

        List<ViewStatsAPIDto> statsDtoList;
        try {
            statsDtoList = Arrays.asList(new ObjectMapper().readValue(response.getBody(), ViewStatsAPIDto[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return statsDtoList;
    }
}
