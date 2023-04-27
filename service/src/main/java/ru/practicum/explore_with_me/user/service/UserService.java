package ru.practicum.explore_with_me.user.service;

import ru.practicum.explore_with_me.user.dto.InputUserDto;
import ru.practicum.explore_with_me.user.dto.OutputUserDto;

import java.util.List;

public interface UserService {
    OutputUserDto registerUser(InputUserDto inputUserDto);

    List<OutputUserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void delete(Long userId);
}
