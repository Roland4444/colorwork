package ru.com.avs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.dao.ScaleDao;
import ru.com.avs.model.Scale;

@Service("ScaleService")
@Transactional
@Scope("prototype")
public class ScaleServiceImpl implements ScaleService {

    @Autowired
    private ScaleDao scaleDao;

    @Override
    public List<Scale> getList() {
        return scaleDao.getList();
    }

    @Override
    public Scale getById(int id) {
        return scaleDao.get(id);
    }

    @Override
    public List<Scale> getByMode(int modeId) {
        return scaleDao.getByMode(modeId);
    }

    @Override
    public void save(Scale scale) {
        if (scale.getId() == 0) {
            scaleDao.save(scale);
        } else {
            scaleDao.update(scale);
        }
    }

    @Override
    public void delete(Scale scale) {
        scaleDao.delete(scale);
    }
}
