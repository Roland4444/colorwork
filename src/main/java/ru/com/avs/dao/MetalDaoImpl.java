package ru.com.avs.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.com.avs.model.Metal;

@Repository("MetalDao")
@Transactional
public class MetalDaoImpl implements MetalDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Metal> getAll() {
        return getSession().createCriteria(Metal.class).list();
    }

    public Metal find(int id) {
        return getSession().get(Metal.class, id);
    }

    @Override
    public void update(List<Metal> metals) {
        getSession().createQuery("delete from Metal").executeUpdate();
        for (Metal metal : metals) {
            getSession().save(metal);
        }
    }
}
