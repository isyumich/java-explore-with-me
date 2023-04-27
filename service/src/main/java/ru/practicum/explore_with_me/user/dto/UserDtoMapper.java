package ru.practicum.explore_with_me.user.dto;

import ru.practicum.explore_with_me.user.model.User;

public class UserDtoMapper {
    public static User inputToModelMapper(InputUserDto inputUserDto) {
        return User.builder()
                .name(inputUserDto.getName())
                .email(inputUserDto.getEmail())
                .build();
    }

    public static OutputUserDto modelToOutputMapper(User user) {
        return OutputUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
