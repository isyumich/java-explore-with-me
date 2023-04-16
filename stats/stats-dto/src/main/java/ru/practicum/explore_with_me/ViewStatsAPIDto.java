package ru.practicum.explore_with_me;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ViewStatsAPIDto {
    String app;
    String uri;
    long hits;
}
