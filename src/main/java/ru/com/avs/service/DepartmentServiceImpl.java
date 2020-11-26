package ru.com.avs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import ru.com.avs.dao.DepartmentDao;
import ru.com.avs.model.Department;

@Service("DepartmentDbService")
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDao dao;

    public DepartmentServiceImpl(DepartmentDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Department> getAll() {
        return dao.getAll();
    }

    @Override
    public void update(List<Map<String, Object>> departments) {
        List<Department> localDepartments = new ArrayList<>();
        for (Map<String, Object> remoteDepartment : departments) {
            Department localDepartment = new Department();
            localDepartment.setId((int) remoteDepartment.get("id"));
            localDepartment.setName(remoteDepartment.get("name").toString());
            localDepartment.setAlias(remoteDepartment.get("alias").toString());
            localDepartment.setType(remoteDepartment.get("type").toString());
            localDepartments.add(localDepartment);
        }
        dao.update(localDepartments);
    }
}
