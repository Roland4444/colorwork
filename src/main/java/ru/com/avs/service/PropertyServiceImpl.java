package ru.com.avs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.dao.PropertyDao;
import ru.com.avs.model.Property;

@Service("PropertyService")
@Transactional
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyDao dao;

    private Map<String, String> properties;

    /**
     * Init.
     */
    @PostConstruct
    public void initProperty() {
        List<Property> propertiesList = this.findAll();
        properties = new HashMap<>();
        for (Property property : propertiesList) {
            properties.put(property.getName(), property.getValue());
        }
    }

    @Override
    public List<Property> findAll() {
        return dao.getList();
    }

    @Override
    public Property find(int id) {
        return dao.get(id);
    }

    @Override
    public Property findByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public void save(Property property) {
        dao.save(property);
        initProperty();
    }

    @Override
    public void delete(int id) {
        Property property = this.find(id);
        dao.delete(property);
        initProperty();
    }

    @Override
    public void update(Property property) {
        dao.update(property);
        initProperty();
    }

    @Override
    public String getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public boolean getBoolProperty(String name) {
        return Boolean.parseBoolean(properties.get(name));
    }

    @Override
    public int getIntProperty(String name) {
        return Integer.parseInt(properties.get(name));
    }
}
