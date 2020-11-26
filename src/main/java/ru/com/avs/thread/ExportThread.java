package ru.com.avs.thread;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.com.avs.model.Waybill;
import ru.com.avs.service.WaybillService;

@Component("ExportThread")
@Scope("prototype")
public class ExportThread extends Thread {

    @Autowired
    private WaybillService waybillService;

    @Override
    public void run() {
        Runnable exporter = () -> {
            List<Waybill> waybills = waybillService.getNotExported();
            for (Waybill waybill : waybills) {
                waybillService.exportWaybill(waybill);
            }
        };
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(exporter, 0, 5, TimeUnit.MINUTES);
    }
}
