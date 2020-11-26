package ru.com.avs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class DaoImpl<T> implements Dao<T> {

    @Autowired
    private SessionFactory sessionFactory;
    private Class<T> type;

    public DaoImpl(Class<T> type) {
        this.type = type;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public T get(int id) {
        return getSession().get(type, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getList() {
        Criteria criteria = getSession().createCriteria(type);
        return (List<T>) criteria.list();
    }

    @Override
    public void save(T entity) {
        getSession().persist(entity);
    }

    @Override
    public void update(T entity) {
        getSession().clear();
        getSession().update(entity);
    }

    @Override
    public void delete(T entity) {
        getSession().delete(entity);
    }

    @Override
    public void delete(int id) {
        T entity = this.get(id);
        this.delete(entity);
    }
}
