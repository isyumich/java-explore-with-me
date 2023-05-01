package ru.practicum.explore_with_me.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InputCommentDto {
    @NotBlank(message = "Текст комментария должен быть указан")
    String text;
}
