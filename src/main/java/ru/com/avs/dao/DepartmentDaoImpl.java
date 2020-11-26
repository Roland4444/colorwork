package ru.com.avs.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.com.avs.model.Department;

@Repository("DepartmentDao")
@Transactional
public class DepartmentDaoImpl implements DepartmentDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Department> getAll() {
        return getSession().createCriteria(Department.class).list();
    }

    @Override
    public void update(List<Department> departments) {
        getSession().createQuery("delete from Department").executeUpdate();
        for (Department department : departments) {
            getSession().save(department);
        }
    }
}
