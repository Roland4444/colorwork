package ru.com.avs.controller;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.com.avs.model.Mode;
import ru.com.avs.model.Scale;
import ru.com.avs.model.ScaleType;
import ru.com.avs.service.ModeService;
import ru.com.avs.service.ScaleService;
import ru.com.avs.view.IntegerField;

@Component
public class ScaleEditController extends AbstractController {

    @Autowired
    private ScaleService scaleService;

    @Autowired
    private ModeService modeService;

    @FXML
    private TextField nameField;
    @FXML
    private TextField portField;
    @FXML
    private IntegerField speedField;
    @FXML
    private ComboBox<ScaleType> typeBox;
    @FXML private TextField commandField;
    @FXML
    private TextArea cameraField;
    @FXML
    private ComboBox<Mode> modeBox;
    @FXML private TextField ipField;
    @FXML private TextField ethPortField;
    @FXML private TextField ethCmdField;

    @FXML private RadioButton rs232Radio;
    @FXML private RadioButton ethernetRadio;
    @FXML private GridPane rsPane;
    @FXML private GridPane ethernetPane;

    private Scale scale;

    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
                if (group.getSelectedToggle() != null) {
                    rsPane.setVisible(rs232Radio.isSelected());
                    ethernetPane.setVisible(ethernetRadio.isSelected());
                }
            }
        });
        rs232Radio.setToggleGroup(group);
        ethernetRadio.setToggleGroup(group);
    }

    void initData(Scale scale) {
        initTypeBox();
        initModeBox();

        commandField.setDisable(true);
        speedField.setDisable(true);

        this.scale = new Scale();

        if (scale != null) {
            this.scale = scale;
            this.nameField.textProperty().setValue(scale.getName());
            this.portField.textProperty().setValue(String.valueOf(scale.getPort()));
            this.typeBox.getSelectionModel().select(scale.getType());
            this.cameraField.textProperty().setValue(scale.getCamera());
            this.modeBox.getSelectionModel().select(scale.getMode());
            if (scale.getConnectionType().equals("rs232")) {
                rs232Radio.setSelected(true);
                rsPane.setVisible(true);
                ethernetPane.setVisible(false);
            } else  {
                ethernetRadio.setSelected(true);
                rsPane.setVisible(false);
                ethernetPane.setVisible(true);
            }
            ipField.setText(scale.getIp());
            ethPortField.setText(scale.getEthPort());
            ethCmdField.setText(scale.getEthCmd());

        } else {
            this.typeBox.getSelectionModel().select(0);
            this.modeBox.getSelectionModel().select(0);
        }
    }

    /**
     * Save or edit Scale.
     */
    @FXML
    public void save() {
        scale.setName(nameField.getText());
        scale.setPort(portField.getText());
        scale.setType(typeBox.getSelectionModel().getSelectedItem());
        scale.setCamera(cameraField.getText());
        scale.setMode(modeBox.getSelectionModel().getSelectedItem());
        String connectionType = rs232Radio.isSelected() ? "rs232" : "ethernet";
        scale.setConnectionType(connectionType);
        scale.setIp(ipField.getText());
        scale.setEthPort(ethPortField.getText());
        scale.setEthCmd(ethCmdField.getText());

        scaleService.save(scale);
        super.save();
    }

    private void initModeBox() {
        ObservableList<Mode> modeData = FXCollections.observableArrayList();
        List<Mode> modes = modeService.getList();
        modeData.addAll(modes);
        modeBox.setItems(modeData);
    }

    private void initTypeBox() {
        typeBox.setItems(FXCollections.observableArrayList(ScaleType.values()));
        typeBox.valueProperty().addListener((scaleType, oldValue, newValue) -> {
            speedField.textProperty().setValue(String.valueOf(scaleType.getValue().getSpeed()));
            commandField.textProperty().setValue(String.valueOf(scaleType.getValue().getCommand()));
        });
    }
}
