package ru.com.avs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.dao.TareDao;
import ru.com.avs.model.Tare;

@Service("TareService")
@Transactional
public class TareServiceImpl implements TareService {

    @Autowired
    private TareDao tareDao;

    @Override
    public List<Tare> getList() {
        return tareDao.getList();
    }

    @Override
    public void delete(Tare tare) {
        tare.setDeleted(true);
        tareDao.update(tare);
    }

    @Override
    public void save(Tare tare) {
        if (tare.getId() == 0) {
            tareDao.save(tare);
        } else {
            tareDao.update(tare);
        }
    }

    @Override
    public Tare getTareById(int id) {
        return tareDao.get(id);
    }

    @Override
    public Tare getTareByIdFromList(List<Tare> tares, int id) {
        return tares.stream()
                .filter(tare -> id == tare.getId())
                .findFirst()
                .orElse(null);
    }
}
