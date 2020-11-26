package ru.com.avs.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.fxml.FXML;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.com.avs.model.Metal;
import ru.com.avs.model.Waybill;
import ru.com.avs.model.Weighing;
import ru.com.avs.service.MetalService;
import ru.com.avs.service.QrService;

@Component
public class PrintController extends AbstractController {

    public ListView nameColumn;
    public ListView weightColumn;
    private Waybill waybill;
    @FXML
    private Label waybillText;
    @FXML
    private Label commentText;
    @FXML
    private Label dateText;
    @FXML
    private AnchorPane draftAnchor;
    @Autowired
    private MetalService metalService;

    @Autowired
    private QrService qrService;

    @FXML
    private ImageView qrImage;

    void initData(Waybill waybill, boolean auto) {
        this.waybill = waybill;

        waybillText.setText("Чек: " + waybill.getWaybill());
        commentText.setText("Клиент: " + waybill.getComment());
        dateText.setText("Дата: " + waybill.getDateCreate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + waybill.getTimeCreate());

        double height = 0;
        List<Weighing> weighings = waybill.getWeighings();
        for (Weighing weighing : weighings) {
            Metal metal = metalService.find(weighing.getMetal());
            nameColumn.getItems().add(metal.getName());
            weightColumn.getItems().add(" " + weighing.getNetto().toString() + " кг");
            height += 25;
        }
        nameColumn.setPrefHeight(height);
        weightColumn.setPrefHeight(height);
        AnchorPane.setTopAnchor(qrImage, 130.0 + height);
        draftAnchor.setPrefHeight(300.0 + height);

        String code = String.valueOf(waybill.getDepartmentId());
        while (code.length() < 3) {
            code = "0" + code;
        }
        code = code + waybill.getId();
        qrImage.setImage(qrService.generate(code));

        if (auto) {
            if (print()) {
                closeThisWindow();
            }
        }
    }

    @FXML
    private boolean print() {
        PrinterJob job = PrinterJob.createPrinterJob();
        Printer printer = job.getPrinter();
        JobSettings jobSettings = job.getJobSettings();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, 0, 0, 0, 0);
        jobSettings.setPageLayout(pageLayout);
        if (job != null) {
            boolean success = job.printPage(draftAnchor);
            if (success) {
                return job.endJob();
            }
        }
        return false;
    }
}
