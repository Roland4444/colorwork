package ru.com.avs.dao;

import java.util.List;
import ru.com.avs.model.Department;

public interface DepartmentDao {

    List<Department> getAll();

    void update(List<Department> departments);
}
