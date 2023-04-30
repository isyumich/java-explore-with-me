package ru.practicum.explore_with_me.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutputUserDto {
    Long id;
    String name;
    String email;
}
