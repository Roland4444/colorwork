package ru.com.avs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.com.avs.dao.TempTareDao;
import ru.com.avs.model.TempTare;

@Service("TempTareService")
public class TempTareServiceImpl implements TempTareService {

    @Autowired
    private TempTareDao tempTareDao;

    @Override
    public void save(TempTare tempTare) {
        tempTareDao.save(tempTare);
    }

    @Override
    public TempTare get(int id) {
        return tempTareDao.get(id);
    }

    @Override
    public void delete(TempTare tare) {
        tempTareDao.delete(tare);
    }
}
