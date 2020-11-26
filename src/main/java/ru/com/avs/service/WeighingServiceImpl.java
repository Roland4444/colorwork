package ru.com.avs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.dao.WeighingDao;
import ru.com.avs.model.Weighing;

@Service("WeighingService")
@Transactional
public class WeighingServiceImpl implements WeighingService {

    @Autowired
    private WeighingDao dao;

    @Override
    public Weighing getById(int id) {
        return dao.get(id);
    }

    @Override
    public void save(Weighing weighing) {
        if (weighing.getId() != 0) {
            dao.update(weighing);
        } else {
            dao.save(weighing);
        }
    }

    @Override
    public void delete(Weighing weighing) {
        //weighing.setDeleted(true);
        //this.save(weighing);
        dao.delete(weighing);
    }

    @Override
    public void delete(Integer weighingId) {
        Weighing weighing = dao.get(weighingId);
        weighing.setDeleted(true);
        this.save(weighing);
    }
}
