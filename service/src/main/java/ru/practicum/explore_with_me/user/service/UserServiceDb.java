package ru.practicum.explore_with_me.user.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.exception.ConflictException;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.user.dto.InputUserDto;
import ru.practicum.explore_with_me.user.dto.OutputUserDto;
import ru.practicum.explore_with_me.user.dto.UserDtoMapper;
import ru.practicum.explore_with_me.user.model.User;
import ru.practicum.explore_with_me.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("UserServiceDb")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceDb implements UserService {
    UserRepository userRepository;

    @Autowired
    public UserServiceDb(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OutputUserDto registerUser(InputUserDto inputUserDto) {
        try {
            return UserDtoMapper.modelToOutputMapper(userRepository.save(UserDtoMapper.inputToModelMapper(inputUserDto)));
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            throw new ConflictException(e.getMessage());
        }

    }

    @Override
    public List<OutputUserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        List<User> users;
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findUsers(pageable);
        } else {
            users = userRepository.findUsersByIds(ids, pageable);
        }
        return userListToOutputUserDtoList(users);
    }

    @Override
    public void delete(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            String message = String.format("%s %d %s", "Пользователь с id =", userId, "не найден");
            log.info(message);
            throw new NotFoundException(message);
        }
        userRepository.delete(userRepository.findById(userId).get());
    }

    private List<OutputUserDto> userListToOutputUserDtoList(List<User> users) {
        List<OutputUserDto> outputUserDtoList = new ArrayList<>();
        for (User user : users) {
            outputUserDtoList.add(UserDtoMapper.modelToOutputMapper(user));
        }
        return outputUserDtoList;
    }
}
