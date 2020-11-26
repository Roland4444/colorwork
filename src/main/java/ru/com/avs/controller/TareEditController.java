package ru.com.avs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.com.avs.model.Tare;
import ru.com.avs.service.TareService;
import ru.com.avs.view.DecimalField;

@Component("TareEditController")
public class TareEditController extends AbstractController {

    private Tare tare;

    @FXML
    private TextField name;
    @FXML
    private DecimalField weight;

    @Autowired
    private TareService tareService;

    void initData(Tare tare) {
        this.tare = new Tare();

        if (tare != null) {
            this.tare = tare;
            this.name.textProperty().setValue(tare.getName());
            this.weight.textProperty().setValue(String.valueOf(tare.getWeight()));
        }
    }

    /**
     * Save or edit Tare.
     */
    @FXML
    public void save() {
        this.tare.setName(name.getText());
        this.tare.setWeight(weight.getDecimal());
        tareService.save(tare);
        super.save();
    }
}
