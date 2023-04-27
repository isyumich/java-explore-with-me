package ru.practicum.explore_with_me.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InputUserDto {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    String name;
    @Email(message = "Email должен содержать символ @")
    @NotBlank(message = "Email пользователя не может быть пустым")
    String email;
}
