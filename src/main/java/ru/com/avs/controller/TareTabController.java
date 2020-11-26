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

import ru.com.avs.model.Tare;
import ru.com.avs.service.TareService;
import ru.com.avs.view.CustomWindowEvent;

@Component("TareController")
public class TareTabController extends AbstractController {

    @FXML
    private Button btnDelete;
    @FXML
    private TableView<Tare> taresTable;
    @FXML
    private TableColumn<Tare, Integer> idTareColumn;
    @FXML
    private TableColumn<Tare, String> nameTareColumn;
    @FXML
    private TableColumn<Tare, String> weightTareColumn;

    @Autowired
    private TareService tareService;

    private Tare tare;
    private String tareEditForm = "settings/tareEditForm";

    /**
     * Initialize.
     */
    public void initialize() {
        refreshTaresData();
        idTareColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTareColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        weightTareColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        taresTable.setEditable(true);
    }

    @FXML
    private void onTareEditStart() {
        Tare tare = taresTable.getSelectionModel().getSelectedItem();
        TareEditController controller =
                (TareEditController) runController(tareEditForm, "Редактирование справочника", true);

        controller.initData(tare);
        Stage stage = controller.getStage();
        stage.setOnCloseRequest(we -> {
            refreshTaresData();
        });
    }

    @FXML
    private void onSelectTare() {
        tare = taresTable.getSelectionModel().getSelectedItem();
        if (tare != null) {
            btnDelete.setDisable(false);
        }
    }

    @FXML
    private void deleteTare() {
        if (tare != null) {
            tareService.delete(tare);
            btnDelete.setDisable(true);
            refreshTaresData();
        }
    }

    @FXML
    private void addTare() {
        TareEditController controller =
                (TareEditController) runController(tareEditForm, "Редактирование справочника", true);
        controller.initData(null);
        Stage stage = controller.getStage();
        stage.setOnCloseRequest(we -> {
            if (we.getEventType() == CustomWindowEvent.SAVE) {
                alert("Успешно сохранено", "Успешно сохранено", Alert.AlertType.INFORMATION);
                refreshTaresData();
            }
        });
    }

    private void refreshTaresData() {
        ObservableList<Tare> tareData = FXCollections.observableArrayList();
        taresTable.getItems().clear();

        List<Tare> tares = tareService.getList();
        for (Tare tare : tares) {
            tareData.add(tare);
        }
        taresTable.setItems(tareData);
    }
}
