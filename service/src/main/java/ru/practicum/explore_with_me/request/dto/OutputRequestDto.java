package ru.practicum.explore_with_me.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore_with_me.constant.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutputRequestDto {
    Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    LocalDateTime created;
    Long event;
    Long requester;
    RequestStatus status;
}
