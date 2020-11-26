package ru.com.avs.dao;

import java.util.List;
import ru.com.avs.model.Metal;

public interface MetalDao {

    List<Metal> getAll();

    Metal find(int id);

    void update(List<Metal> departments);
}
