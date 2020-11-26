package ru.com.avs.dao;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.model.Waybill;

@Repository("WaybillDao")
@Transactional
public class WaybillDaoImpl extends DaoImpl<Waybill> implements WaybillDao {

    public WaybillDaoImpl() {
        super(Waybill.class);
    }

    @Override
    public List<Waybill> search(LocalDate dateStart, LocalDate dateEnd, String comment) {
        Criteria criteria = getSession().createCriteria(Waybill.class, "waybill");

        if (dateStart != null && dateEnd != null) {
            criteria.add(Restrictions.between("dateCreate", dateStart, dateEnd));
        }
        if (comment != null && !comment.isEmpty()) {
            criteria.add(Restrictions.like("comment", comment + "%").ignoreCase());
        }

        criteria.addOrder(Order.asc("id")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public List<Waybill> getNotExported() {
        Criteria criteria = getSession().createCriteria(Waybill.class);
        criteria.add(Restrictions.eq("upload", false));
        criteria.add(Restrictions.eq("complete", true));
        criteria.addOrder(Order.asc("id")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public List<Waybill> getNotCompleted() {
        Criteria criteria = getSession().createCriteria(Waybill.class);
        criteria.add(Restrictions.eq("complete", false));
        criteria.add(Restrictions.eq("dateCreate", LocalDate.now()));
        criteria.addOrder(Order.asc("id")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public Waybill getLastWaybill() {
        Criteria criteria = getSession().createCriteria(Waybill.class);
        criteria.add(Restrictions.eq("dateCreate", LocalDate.now()));
        criteria.addOrder(Order.desc("id")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setMaxResults(1);
        return (Waybill) criteria.uniqueResult();
    }
}
