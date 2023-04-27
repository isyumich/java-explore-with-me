package ru.practicum.explore_with_me.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InputCompilationDto {
    List<Long> events;
    Boolean pinned;
    @NotBlank(message = "Название подборки должно быть указано")
    String title;
}
