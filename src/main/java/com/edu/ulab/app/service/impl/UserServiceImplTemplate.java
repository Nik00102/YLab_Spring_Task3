package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImplTemplate implements UserService {
    private final JdbcTemplate jdbcTemplate;

    public UserServiceImplTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        final String INSERT_SQL = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    return ps;
                }, keyHolder);

        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        // реализовать недстающие методы
        String name = userDto.getFullName();
        String title = userDto.getTitle();
        int age = userDto.getAge();

        UserDto updatedUser = getUserById(userId);
        updatedUser.setAge(age);
        updatedUser.setTitle(title);
        updatedUser.setFullName(name);
        log.info("Updated person: {}", updatedUser);
        final String UPDATE_PERSON_SQL = "UPDATE PERSON SET FULL_NAME = ? , TITLE = ? , AGE = ? WHERE ID = ?";
        //List<UserDto> users = jdbcTemplate.query(UPDATE_PERSON_SQL, new BeanPropertyRowMapper<UserDto>(UserDto.class));
        jdbcTemplate.update(UPDATE_PERSON_SQL, new Object[] {userDto.getFullName(), userDto.getTitle(), userDto.getAge(), userId});
        return updatedUser;
    }

    @Override
    public UserDto getUserById(Long userId) {
        // реализовать недстающие методы
        final String GET_PERSON_SQL = "SELECT * FROM PERSON WHERE ID = ?";
        List<UserDto> users = jdbcTemplate.query(GET_PERSON_SQL, new BeanPropertyRowMapper<UserDto>(UserDto.class), userId);
        return users.get(0);
    }

    @Override
    public boolean deleteUserById(Long userId) {
        // реализовать недстающие методы
        final String DELETE_PERSON_BY_ID = "DELETE FROM PERSON WHERE ID = ?";
        jdbcTemplate.update(DELETE_PERSON_BY_ID, userId);
        return getUserById(userId)!=null;
    }
}
