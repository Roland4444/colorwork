package ru.com.avs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import ru.com.avs.dao.MetalDao;
import ru.com.avs.model.Metal;

@Service("MetalService")
public class MetalServiceImpl implements MetalService {

    private final MetalDao dao;

    public MetalServiceImpl(MetalDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Metal> getAll() {
        return dao.getAll();
    }

    @Override
    public Metal find(int id) {
        return dao.find(id);
    }

    @Override
    public void update(List<Map<String, Object>> metals) {
        List<Metal> localDepartments = new ArrayList<>();
        for (Map<String, Object> remoteMetal : metals) {
            Metal localMetal = new Metal();
            localMetal.setId((int) remoteMetal.get("id"));
            localMetal.setName(remoteMetal.get("name").toString());
            localDepartments.add(localMetal);
        }
        dao.update(localDepartments);
    }
}
