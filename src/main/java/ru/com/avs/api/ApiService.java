package ru.com.avs.api;

import java.util.List;
import java.util.Map;

public interface ApiService {

    Token getToken();

    List<Map<String, Object>> getDepartments();

    List<Map<String, Object>> getMetals();

    boolean exportWaybill(Map<String, Object> map);
}
