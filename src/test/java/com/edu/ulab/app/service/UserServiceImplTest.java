package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person  = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson  = new Person();
        savedPerson.setId(1L);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");


        //when

        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);


        //then

        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
    }

    // update
    @Test
    @DisplayName("Обновление пользователя. Должно пройти успешно.")
    void updatePerson_Test() {

        //given
        Long userId = 1L;

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person  = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");
        person.setId(userId);

        UserDto userUpdatedDto = new UserDto();
        userUpdatedDto.setAge(50);
        userUpdatedDto.setFullName("updated name");
        userUpdatedDto.setTitle("updated title");

        Person updatedPerson  = new Person();
        updatedPerson.setId(userId);
        updatedPerson.setFullName("updated name");
        updatedPerson.setAge(50);
        updatedPerson.setTitle("updated title");

        UserDto result = new UserDto();
        result.setId(userId);
        result.setAge(50);
        result.setFullName("updated name");
        result.setTitle("updated title");

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(person));
        when(userMapper.personToUserDto(person)).thenReturn(userDto);
        when(userService.updateUser(userUpdatedDto,userId)).thenReturn(result);


        //then
        UserDto userDtoResult = userService.updateUser(userUpdatedDto,userId);
        assertEquals(50, userDtoResult.getAge());
    }

    // get
    @Test
    @DisplayName("Поиск пользователя по id. Должно пройти успешно.")
    void getPerson_Test() {
        //given
        Long userId = 1L;

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person  = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");
        person.setId(userId);

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(person));
        when(userMapper.personToUserDto(person)).thenReturn(userDto);

        //then
        UserDto userDtoResult = userService.getUserById(userId);
        assertEquals(1L, userDtoResult.getId());
    }


    // delete
    @Test
    @DisplayName("Удаление пользователя по id. Должно пройти успешно.")
    void deletePerson_Test() {
        //given
        Long userId = 1L;

        Person person  = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");
        person.setId(1L);

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(person));


        //then
        assertEquals(true, userService.deleteUserById(1L));

    }
    // * failed
    //         doThrow(dataInvalidException).when(testRepository)
    //                .save(same(test));
    // example failed
    //  assertThatThrownBy(() -> testeService.createTest(testRequest))
    //                .isInstanceOf(DataInvalidException.class)
    //                .hasMessage("Invalid data set");
}
