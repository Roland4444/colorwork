package ru.com.avs.dao;

import org.springframework.stereotype.Repository;

import ru.com.avs.model.TempTare;

@Repository("TempTareDao")
public class TempTareDaoImpl extends DaoImpl<TempTare> implements TempTareDao {

    public TempTareDaoImpl() {
        super(TempTare.class);
    }
}
