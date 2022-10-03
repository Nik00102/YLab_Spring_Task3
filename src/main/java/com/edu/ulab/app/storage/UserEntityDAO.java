package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Person;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class UserEntityDAO implements DAO<Person>{
    private static Map<Long, Person> users = new HashMap<>();
    private static Long index = 0L;

    //получить читателя по id
    public Optional<Person> get(long id) {
        return Optional.of(users.get(id));
    }

    //список всех читателей
    @Override
    public List<Person> getAll() {
        return users.values().stream().toList();
    }

    //добавление нового читателя
    @Override
    public Person save(Person userEntity) {
        if (!users.containsValue(userEntity)) {
            index++;
            userEntity.setId(index);
            users.put(index,userEntity);
            log.info("Created userEntity: {}", userEntity);
        } else {
            Long id = users.entrySet().stream().filter(entry->entry.getValue().equals(userEntity)).findFirst().get().getKey();
            userEntity.setId(id);
            log.info("Couldn't create userEntity (storage already have this userEntity) : {}", userEntity);
        }
        return userEntity;
    }

    //обновление данных читателя
    @Override
    public Person update(Person userEntity, Long userId) {
        Person user = null;
        if (!users.containsKey(userId)) {
            save(userEntity);
        } else {
            user = users.entrySet()
                    .stream()
                    .filter(entry->entry.getValue().getId().equals(userId))
                    .findFirst().get().getValue();
            user.setAge(userEntity.getAge());
            user.setFullName(userEntity.getFullName());
            log.info("Updated userEntity: {}", user);
        }
        return user;
    }

    //удаление читателя
    public void delete(Person userEntity) {
        if (users.containsKey(userEntity.getId())) {
            users.remove(userEntity.getId());
            log.info("Deleted userEntity: {}", userEntity);
        } else
            log.info("Couldn't delete userEntity (storage doesn't have this userEntity) : {}", userEntity);
    }
}
