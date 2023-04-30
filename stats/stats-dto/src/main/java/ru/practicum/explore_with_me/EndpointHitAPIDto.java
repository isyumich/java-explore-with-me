package ru.practicum.explore_with_me;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHitAPIDto {
    //@NotBlank
    String app;
    // @NotBlank
    String uri;
    //@NotBlank
    String ip;
    // @NotBlank
    String timestamp;
}
