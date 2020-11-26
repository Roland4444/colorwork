package ru.com.avs.service;

import java.time.LocalDate;
import java.util.List;

import ru.com.avs.model.Waybill;

public interface WaybillService {
    void save(Waybill waybill);

    List<Waybill> search(LocalDate dateStart, LocalDate dateEnd, String comment);

    int getLastWaybillNumber();

    void delete(Waybill waybill);

    Waybill getById(int id);

    List<Waybill> getNotExported();

    List<Waybill> getNotCompleted();

    boolean exportWaybill(Waybill waybill);
}
