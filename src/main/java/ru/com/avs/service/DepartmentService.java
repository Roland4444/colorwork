package ru.com.avs.service;

import java.util.List;
import java.util.Map;
import ru.com.avs.model.Department;

public interface DepartmentService {

    List<Department> getAll();

    void update(List<Map<String, Object>> departments);

}
