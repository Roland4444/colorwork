package ru.com.avs.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.com.avs.model.Scale;
import ru.com.avs.service.ScaleService;
import ru.com.avs.view.CustomWindowEvent;

@Component
public class ScaleTabController extends AbstractController {

    @FXML
    private TableColumn<Scale, Integer> scaleIdColumn;
    @FXML
    private TableColumn<Scale, String> scaleNameColumn;
    @FXML
    private TableColumn<Scale, Integer> scalePortColumn;
    @FXML
    private TableColumn<Scale, Integer> scaleTypeColumn;
    @FXML
    private TableColumn<Scale, String> scaleCameraColumn;
    @FXML
    private TableColumn<Scale, String> scaleModeColumn;
    @FXML
    private TableView<Scale> scaleTable;
    @FXML
    private Button scaleDeleteButton;

    @Autowired
    private ScaleService scaleService;

    private Scale scale;
    private String scaleEditForm = "settings/scaleEditForm";

    /**
     * Initialize.
     */
    public void initialize() {
        refreshScalesData();
        scaleIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        scaleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scalePortColumn.setCellValueFactory(new PropertyValueFactory<>("port"));
        scaleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        scaleCameraColumn.setCellValueFactory(new PropertyValueFactory<>("camera"));
        scaleModeColumn.setCellValueFactory(new PropertyValueFactory<>("mode"));
        scaleTable.setEditable(true);
    }

    @FXML
    private void onScaleEdit() {
        Scale scale = scaleTable.getSelectionModel().getSelectedItem();
        ScaleEditController controller =
                (ScaleEditController) runController(scaleEditForm, "Редактирование весов", false);

        controller.initData(scale);
        Stage stage = controller.getStage();
        stage.setOnCloseRequest(we -> {
            if (we.getEventType() == CustomWindowEvent.SAVE) {
                alert("Успешно сохранено", "Успешно сохранено", Alert.AlertType.INFORMATION);
                refreshScalesData();
            }
        });
    }

    @FXML
    private void addScale() {
        ScaleEditController controller =
                (ScaleEditController) runController(scaleEditForm, "Редактирование весов", false);
        controller.initData(null);
        Stage stage = controller.getStage();
        stage.setOnCloseRequest(we -> {
            if (we.getEventType() == CustomWindowEvent.SAVE) {
                alert("Успешно сохранено", "Успешно сохранено", Alert.AlertType.INFORMATION);
                refreshScalesData();
            }
        });
    }

    @FXML
    private void deleteScale() {
        if (scale != null) {
            scaleService.delete(scale);
            scaleDeleteButton.setDisable(true);
            refreshScalesData();
        }
    }

    @FXML
    private void onSelectScale() {
        scale = scaleTable.getSelectionModel().getSelectedItem();
        if (scale != null) {
            scaleDeleteButton.setDisable(false);
        }
    }

    private void refreshScalesData() {
        ObservableList<Scale> scaleData = FXCollections.observableArrayList();
        scaleTable.getItems().clear();

        List<Scale> scales = scaleService.getList();
        for (Scale scale : scales) {
            scaleData.add(scale);
        }
        scaleTable.setItems(scaleData);
    }
}
