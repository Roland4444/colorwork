package ru.com.avs.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.model.Property;

@Repository("PropertyDao")
@Transactional
public class PropertyDaoImpl extends DaoImpl<Property> implements PropertyDao {

    public PropertyDaoImpl() {
        super(Property.class);
    }

    @Override
    public Property findByName(String name) {
        Criteria criteria = getSession().createCriteria(Property.class);
        criteria.add(Restrictions.eq("name", name));
        return (Property) criteria.uniqueResult();
    }
}
