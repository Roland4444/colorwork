package ru.com.avs.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.com.avs.model.Metal;
import ru.com.avs.model.ViewModel;
import ru.com.avs.model.Waybill;
import ru.com.avs.model.Weighing;
import ru.com.avs.service.MetalService;
import ru.com.avs.service.WaybillService;
import ru.com.avs.service.WeighingService;
import ru.com.avs.view.DecimalField;

@Component("WaybillEditController")
public class WaybillEditController extends AbstractController {

    private Weighing weighing;

    @FXML
    private DatePicker dateText;
    @FXML
    private TextField commentText;
    @FXML
    private TextField waybillText;
    @FXML
    private DecimalField bruttoText;
    @FXML
    private DecimalField nettoText;
    @FXML
    private DecimalField cloggingText;
    @FXML
    private DecimalField trashText;
    @FXML
    private DecimalField tareText;
    @FXML
    private ComboBox<Metal> metalBox;

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private WeighingService weighingService;

    @Autowired
    private MetalService metalService;

    void initData(ViewModel viewModel) {
        initMetals();
        weighing = weighingService.getById(viewModel.getWeighingId());
        dateText.setValue(weighing.getWaybill().getDateCreate());
        commentText.setText(weighing.getWaybill().getComment());
        waybillText.setText(String.valueOf(weighing.getWaybill().getWaybill()));

        bruttoText.setDecimal(weighing.getBrutto());
        nettoText.setDecimal(weighing.getNetto());
        cloggingText.setDecimal(weighing.getClogging());
        trashText.setDecimal(weighing.getTrash());
        tareText.setDecimal(viewModel.getTare());
        metalBox.getSelectionModel().select(viewModel.getMetal());
    }

    /**
     * Save or edit Waybill.
     */
    @FXML
    public void save() {
        Waybill newWaybill = new Waybill();
        newWaybill.setWaybill(Integer.parseInt(waybillText.getText()));
        newWaybill.setComment(commentText.getText());
        newWaybill.setDateCreate(dateText.getValue());
        Waybill oldWaybill = weighing.getWaybill();

        if (!newWaybill.equals(oldWaybill)) {
            oldWaybill.concat(newWaybill);
            waybillService.save(oldWaybill);
        }
        weighing.setBrutto(bruttoText.getDecimal());
        weighing.setNetto(nettoText.getDecimal());
        weighing.setClogging(cloggingText.getDecimal());
        weighing.setTrash(trashText.getDecimal());
        weighing.setTare(tareText.getDecimal());
        weighing.setMetal(metalBox.getSelectionModel().getSelectedItem().getId());
        weighingService.save(weighing);

        super.save();
    }

    private void initMetals() {
        ObservableList<Metal> metalsData = FXCollections.observableArrayList();
        List<Metal> metals = metalService.getAll();
        metalsData.addAll(metals);
        metalBox.setItems(metalsData);
    }
}
