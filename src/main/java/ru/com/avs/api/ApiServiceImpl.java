package ru.com.avs.api;

import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service("ApiService")
class ApiServiceImpl implements ApiService {

    private final RequestDao requestDao;
    private Token token = null;

    ApiServiceImpl(RequestDao requestDao) {
        this.requestDao = requestDao;
    }

    private Token requestToken() {
        ClientResponse response = requestDao.requestToken();
        return response.getEntity(Token.class);
    }

    @Override
    public Token getToken() {
        boolean expired = true;
        if (expired) {
            this.token = requestToken();
        }
        return this.token;
    }

    @Override
    public List<Map<String, Object>> getDepartments() {
        Token token = getToken();
        ClientResponse response = requestDao.getDepartments(token);

        if (response.getStatus() == HttpStatus.OK.value()) {
            Department.DepartmentData jsonData = response.getEntity(Department.DepartmentData.class);
            if (jsonData != null) {
                List<Department> parsedData = jsonData.getDepartmentList();
                List<Map<String, Object>> result = new ArrayList<>();
                for (Department department : parsedData) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", department.getId());
                    map.put("name", department.getName());
                    map.put("alias", department.getAlias());
                    map.put("type", department.getType());
                    result.add(map);
                }
                return result;
            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getMetals() {
        Token token = getToken();
        ClientResponse response = requestDao.getMetals(token);

        int i = 0;
        //todo странная логика походу для обновления токена при протухании. убрать когда добавим expire time
        while (response.getStatus() == HttpStatus.UNAUTHORIZED.value() || i < 3) {
            token = getToken();
            response = requestDao.getMetals(token);
            i++;
        }

        if (response.getStatus() == HttpStatus.OK.value()) {
            Metal.MetalsData listMetals = response.getEntity(Metal.MetalsData.class);
            if (listMetals != null) {
                List<Metal> metals = listMetals.getMetalList();
                List<Map<String, Object>> result = new ArrayList<>();
                for (Metal metal : metals) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", metal.getId());
                    map.put("name", metal.getName());
                    result.add(map);
                }
                return result;
            }
        }
        return null;
    }

    @Override
    public boolean exportWaybill(Map<String, Object> map) {
        WaybillJson waybill = waybillFromMap(map);
        try {
            String json = new ObjectMapper().writeValueAsString(waybill);

        Token token = getToken();
        ClientResponse response = requestDao.exportWaybill(json, token);

        //todo странная логика походу для обновления токена при протухании. убрать когда добавим expire time
        for (int i = 0; response.getStatus() == HttpStatus.UNAUTHORIZED.value() && i < 3; i++) {
            token = getToken();
            response = requestDao.exportWaybill(json, token);
        }
        return response.getStatus() == HttpStatus.OK.value();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private WaybillJson waybillFromMap(Map<String, Object> waybill) {
        List<Map<String, Object>> weighings = (List<Map<String, Object>>) waybill.get("weighings");
        WaybillJsonItem waybillJsonItem;
        List<WaybillJsonItem> items = new ArrayList<>();
        for (Map<String, Object> weighing : weighings) {
            waybillJsonItem = new WaybillJsonItem();
            waybillJsonItem.setBrutto((BigDecimal) weighing.get("brutto"));
            waybillJsonItem.setMetalId((int) weighing.get("metal"));
            waybillJsonItem.setClogging((BigDecimal) weighing.get("clogging"));
            waybillJsonItem.setTrash((BigDecimal) weighing.get("trash"));
            waybillJsonItem.setTare((BigDecimal) weighing.get("tare"));

            waybillJsonItem.setPhotoPreview((String) weighing.get("photoPreview"));
            waybillJsonItem.setPhotoFull((String) weighing.get("photoFull"));
            items.add(waybillJsonItem);
        }
        WaybillJson waybillJson = new WaybillJson();
        waybillJson.setWaybill((int) waybill.get("waybill"));
        waybillJson.setComment((String) waybill.get("comment"));
        waybillJson.setDate((String) waybill.get("date"));
        waybillJson.setTime((String) waybill.get("time"));
        waybillJson.setDepartmentId((int) waybill.get("departmentId"));
        waybillJson.setId((int) waybill.get("waybill"));
        waybillJson.setMode((String) waybill.get("mode"));
        waybillJson.setWeighings(items);
        return waybillJson;
    }
}
