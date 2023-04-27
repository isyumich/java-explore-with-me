package ru.practicum.explore_with_me.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore_with_me.event.model.Event;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutputCompilationDto {
    List<Event> events;
    Long id;
    Boolean pinned;
    String title;
}
