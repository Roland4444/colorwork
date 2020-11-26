package ru.com.avs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.model.Scale;

@Repository("ScalesDao")
@Transactional
public class ScaleDaoImpl extends DaoImpl<Scale> implements ScaleDao {

    public ScaleDaoImpl() {
        super(Scale.class);
    }

    @Override
    public List<Scale> getByMode(int modeId) {
        Criteria criteria = getSession().createCriteria(Scale.class);
        criteria.add(Restrictions.eq("mode.id", modeId));
        return (List<Scale>) criteria.list();
    }
}
