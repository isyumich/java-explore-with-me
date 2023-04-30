package ru.practicum.explore_with_me.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore_with_me.constant.EventChangeStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InputEventDto {
    @NotBlank(message = "Аннотация события должна быть указана")
    String annotation;
    @NotNull(message = "Id категории события должен быть указан")
    Long category;
    @NotBlank(message = "Описание события должно быть указано")
    String description;
    @NotNull(message = "Дата события должна быть указана")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    LocalDateTime eventDate;
    @NotNull(message = "Локация события должна быть указана")
    InputLocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    EventChangeStatus stateAction;
    @NotBlank(message = "Название события должно быть заполнено")
    String title;
}
