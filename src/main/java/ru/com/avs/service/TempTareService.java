package ru.com.avs.service;

import ru.com.avs.model.TempTare;

public interface TempTareService {
    void save(TempTare tempTare);

    TempTare get(int id);

    void delete(TempTare tare);
}
