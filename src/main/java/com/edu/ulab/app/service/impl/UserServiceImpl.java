package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.mapper.UserMapperImpl;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        Person savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        // реализовать недстающие методы
        UserDto userForUpdate = getUserById(userId);
        userForUpdate.setFullName(userDto.getFullName());
        userForUpdate.setTitle(userDto.getTitle());
        userForUpdate.setAge(userDto.getAge());
        return createUser(userForUpdate);
    }

    @Override
    public UserDto getUserById(Long id) {
        // реализовать недстающие методы
        return new UserMapperImpl().personToUserDto(userRepository.findById(id).get());
    }

    @Override
    public boolean deleteUserById(Long id) {
        // реализовать недстающие методы
        Person person = userMapper.userDtoToPerson(getUserById(id));
        userRepository.delete(person);
        log.info("Deleted person: {}", person);
        return getUserById(id)!=null;
    }
}
