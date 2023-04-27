package ru.practicum.explore_with_me.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explore_with_me.constant.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InputRequestsStatusDto {
    @NotNull
    List<Long> requestIds;
    @NotNull
    RequestStatus status;
}
