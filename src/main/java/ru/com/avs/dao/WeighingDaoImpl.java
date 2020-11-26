package ru.com.avs.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.model.Weighing;

@Repository("WeighingDao")
@Transactional
public class WeighingDaoImpl extends DaoImpl<Weighing> implements WeighingDao {

    public WeighingDaoImpl() {
        super(Weighing.class);
    }
}
