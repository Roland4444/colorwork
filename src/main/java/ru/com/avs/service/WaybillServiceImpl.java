package ru.com.avs.service;

import static ru.com.avs.util.UserUtils.fileToBase64;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.com.avs.api.ApiService;
import ru.com.avs.dao.WaybillDao;
import ru.com.avs.model.Waybill;
import ru.com.avs.model.Weighing;

@Service("WaybillService")
@Transactional
public class WaybillServiceImpl implements WaybillService {

    private static final Logger log = LoggerFactory.getLogger(WaybillServiceImpl.class);

    @Autowired
    private WaybillDao waybillDao;

    @Autowired
    private PropertyService property;

    @Autowired
    private ApiService apiService;

    @Override
    public void save(Waybill waybill) {
        if (waybill.getId() != 0) {
            waybillDao.update(waybill);
        } else {
            waybillDao.save(waybill);
        }
    }

    @Override
    public List<Waybill> search(LocalDate dateStart, LocalDate dateEnd, String comment) {
        return waybillDao.search(dateStart, dateEnd, comment);
    }

    @Override
    public int getLastWaybillNumber() {
        Waybill waybill = waybillDao.getLastWaybill();

        if (waybill != null) {
            return waybill.getWaybill() + 1;
        } else {
            return 1;
        }
    }

    @Override
    public void delete(Waybill waybill) {
        waybillDao.delete(waybill);
    }

    @Override
    public Waybill getById(int id) {
        return waybillDao.get(id);
    }

    @Override
    public List<Waybill> getNotExported() {
        return waybillDao.getNotExported();
    }

    @Override
    public List<Waybill> getNotCompleted() {
        return waybillDao.getNotCompleted();
    }

    @Override
    public boolean exportWaybill(Waybill waybill) {
        Map<String, Object> map = waybillToMap(waybill);

        if (apiService.exportWaybill(map)) {
            waybill.setUpload(true);
            this.save(waybill);
            return true;
        } else {
            log.error("Can't export waybill");
            return false;
        }
    }

    private Map<String, Object> waybillToMap(Waybill waybill) {
        List<Weighing> weighings = waybill.getWeighings();
        List<Map<String, Object>> items = new ArrayList<>();
        String basePath = property.getProperty("image.path") + waybill.getDateCreate() + "/";
        for (Weighing weighing : weighings) {
            Map<String, Object> item = new HashMap<>();
            item.put("brutto", weighing.getBrutto());
            item.put("metal", weighing.getMetal());
            item.put("clogging", weighing.getClogging());
            item.put("trash", weighing.getTrash());
            item.put("tare", weighing.getTare());

            String previewPath = basePath + "preview-" + weighing.getId() + ".jpg";
            String fullPath = basePath + "snapshot-" + weighing.getId() + ".jpg";
            item.put("photoPreview", fileToBase64(previewPath));
            item.put("photoFull", fileToBase64(fullPath));
            items.add(item);
        }
        Map<String, Object> waybillJson = new HashMap<>();
        waybillJson.put("waybill", waybill.getWaybill());
        waybillJson.put("comment", waybill.getComment());
        waybillJson.put("date", String.valueOf(waybill.getDateCreate()));
        waybillJson.put("time", String.valueOf(waybill.getTimeCreate()));
        waybillJson.put("departmentId", waybill.getDepartmentId());
        waybillJson.put("id", waybill.getWaybill());
        waybillJson.put("mode", waybill.getMode());
        waybillJson.put("weighings", items);
        return waybillJson;
    }
}
