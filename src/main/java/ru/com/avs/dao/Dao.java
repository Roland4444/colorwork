package ru.com.avs.dao;

import java.util.List;

public interface Dao<T> {

    T get(int id);

    List<T> getList();

    void save(T entity);

    void update(T entity);

    void delete(T entity);

    void delete(int id);
}
