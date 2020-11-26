package ru.com.avs.service;

import java.util.List;

import ru.com.avs.model.Tare;

public interface TareService {
    List<Tare> getList();

    void save(Tare tare);

    void delete(Tare tare);

    Tare getTareById(int id);

    Tare getTareByIdFromList(List<Tare> tares, int id);
}
