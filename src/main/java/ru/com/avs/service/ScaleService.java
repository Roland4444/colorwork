package ru.com.avs.service;

import java.util.List;

import ru.com.avs.model.Scale;

public interface ScaleService {

    List<Scale> getList();

    Scale getById(int id);

    List<Scale> getByMode(int modeId);

    void save(Scale scale);

    void delete(Scale scale);
}
