package ru.com.avs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.dao.ModeDao;
import ru.com.avs.model.Mode;

@Service("ModeService")
@Transactional
public class ModeServiceImpl implements ModeService {

    @Autowired
    private ModeDao modeDao;

    @Autowired
    private PropertyService propertyService;

    @Override
    public List<Mode> getList() {
        return modeDao.getList();
    }

    @Override
    public Mode getById(int id) {
        return modeDao.get(id);
    }

    @Override
    public Mode getActiveMode() {
        int modeId = propertyService.getIntProperty("mode");
        return this.getById(modeId);
    }

    @Override
    public Mode getByCode(List<Mode> modes, String modeCode) {
        return modes.stream()
                .filter(mode -> modeCode.equals(mode.getModeCode()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Mode mode) {
        if (mode.getId() == 0) {
            modeDao.save(mode);
        } else {
            modeDao.update(mode);
        }
    }

    @Override
    public void delete(Mode mode) {
        modeDao.delete(mode);
    }
}
