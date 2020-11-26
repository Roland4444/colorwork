package ru.com.avs.service;

import ru.com.avs.model.Weighing;

public interface WeighingService {

    Weighing getById(int id);

    void save(Weighing weighing);

    void delete(Weighing weighing);

    void delete(Integer weighingId);
}
