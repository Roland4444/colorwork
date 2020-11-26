package ru.com.avs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.com.avs.model.Mode;
import ru.com.avs.service.ModeService;

@Component
public class ModeEditController extends AbstractController {

    @Autowired
    private ModeService service;
    @FXML
    private TextField nameText;
    @FXML
    private TextField codeText;

    private Mode mode;

    void initData(Mode mode) {
        this.mode = new Mode();

        if (mode != null) {
            this.mode = mode;
            this.nameText.textProperty().setValue(mode.getName());
            this.codeText.textProperty().setValue(mode.getModeCode());
        }
    }

    /**
     * Save or update Mode.
     */
    @FXML
    public void save() {
        mode.setName(nameText.getText());
        mode.setModeCode(codeText.getText());
        service.save(mode);
        super.save();
    }
}
