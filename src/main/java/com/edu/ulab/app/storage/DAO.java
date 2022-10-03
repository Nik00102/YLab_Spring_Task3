package com.edu.ulab.app.storage;

import java.util.List;

public interface DAO<T> {
    List<T> getAll();

    T save(T t);

    T update(T t, Long userId);

}
