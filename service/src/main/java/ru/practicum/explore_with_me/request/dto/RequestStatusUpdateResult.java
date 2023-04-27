package ru.practicum.explore_with_me.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestStatusUpdateResult {
    List<OutputRequestDto> confirmedRequests;
    List<OutputRequestDto> rejectedRequests;
}
