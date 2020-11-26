package ru.com.avs.service;

import java.util.List;

import ru.com.avs.model.Property;

public interface PropertyService {
    List<Property> findAll();

    Property find(int id);

    Property findByName(String name);

    void save(Property property);

    void delete(int id);

    void update(Property property);

    String getProperty(String name);

    boolean getBoolProperty(String name);

    int getIntProperty(String name);
}
