package ru.com.avs.dao;

import ru.com.avs.model.Property;

public interface PropertyDao extends Dao<Property> {
    Property findByName(String name);
}
