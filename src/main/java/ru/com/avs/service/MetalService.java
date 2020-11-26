package ru.com.avs.service;

import java.util.List;
import java.util.Map;

import ru.com.avs.model.Metal;

public interface MetalService {

    List<Metal> getAll();

    Metal find(int id);

    void update(List<Map<String, Object>> metals);
}
