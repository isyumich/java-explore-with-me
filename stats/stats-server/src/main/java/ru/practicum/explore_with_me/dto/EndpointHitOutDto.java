package ru.practicum.explore_with_me.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHitOutDto {
    Long id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
