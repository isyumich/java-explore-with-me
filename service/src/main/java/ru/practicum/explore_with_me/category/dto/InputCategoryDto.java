package ru.practicum.explore_with_me.category.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InputCategoryDto {
    @NotBlank(message = "Имя категории должно быть указано")
    String name;
}
