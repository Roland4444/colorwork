package ru.com.avs.dao;

import java.time.LocalDate;
import java.util.List;

import ru.com.avs.model.Waybill;

public interface WaybillDao extends Dao<Waybill> {
    List<Waybill> search(LocalDate dateStart, LocalDate dateEnd, String comment);

    List<Waybill> getNotExported();

    List<Waybill> getNotCompleted();

    Waybill getLastWaybill();
}
