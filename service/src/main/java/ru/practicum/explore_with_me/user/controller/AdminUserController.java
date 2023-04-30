package ru.practicum.explore_with_me.user.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.user.dto.InputUserDto;
import ru.practicum.explore_with_me.user.dto.OutputUserDto;
import ru.practicum.explore_with_me.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUserController {
    final UserService userService;

    @Autowired
    public AdminUserController(@Qualifier("UserServiceDb") UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OutputUserDto registerUser(@Valid @RequestBody InputUserDto inputUserDto) {
        log.info("Запрос на создание нового пользователя");
        return userService.registerUser(inputUserDto);
    }

    @GetMapping
    public List<OutputUserDto> getUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                        @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка всех пользователей");
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "userId") Long userId) {
        log.info(String.format("%s %d", "Запрос на удаление пользователя с id =", userId));
        userService.delete(userId);
    }
}
