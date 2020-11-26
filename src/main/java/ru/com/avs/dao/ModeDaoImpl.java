package ru.com.avs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.model.Mode;

@Repository("ModeDao")
@Transactional
public class ModeDaoImpl extends DaoImpl<Mode> implements ModeDao {

    public ModeDaoImpl() {
        super(Mode.class);
    }

    @Override
    public List<Mode> getList() {
        Criteria criteria = getSession().createCriteria(Mode.class);
        criteria.addOrder(Order.asc("id")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }
}


