package ru.com.avs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.model.Tare;

@Repository("TareDao")
@Transactional
public class TareDaoImpl extends DaoImpl<Tare> implements TareDao {

    public TareDaoImpl() {
        super(Tare.class);
    }

    @Override
    public List<Tare> getList() {
        Criteria criteria = getSession().createCriteria(Tare.class);
        criteria.add(Restrictions.eq("deleted", false));
        return criteria.list();
    }

    @Override
    public Tare get(int id) {
        Criteria criteria = getSession().createCriteria(Tare.class);
        criteria.add(Restrictions.eq("id", id));
        criteria.add(Restrictions.eq("deleted", false));
        return (Tare) criteria.uniqueResult();
    }
}
