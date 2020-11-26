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

import ru.com.avs.model.Mode;
import ru.com.avs.service.ModeService;
import ru.com.avs.view.CustomWindowEvent;

@Component
public class ModeTabController extends AbstractController {

    @FXML
    private TableColumn<Mode, Integer> modeIdColumn;
    @FXML
    private TableColumn<Mode, String> modeNameColumn;
    @FXML
    private TableColumn<Mode, String> modeCodeColumn;
    @FXML
    private TableView<Mode> modeTable;

    @Autowired
    private ModeService modeService;

    @FXML
    private Button modeDeleteButton;
    private Mode mode;
    private String modeEditForm = "settings/modeEditForm";

    /**
     * Initialize.
     */
    public void initialize() {
        refreshModesData();
        modeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modeCodeColumn.setCellValueFactory(new PropertyValueFactory<>("modeCode"));
        modeTable.setEditable(true);
    }

    @FXML
    private void onModeEdit() {
        Mode mode = modeTable.getSelectionModel().getSelectedItem();
        ModeEditController controller =
                (ModeEditController) runController(modeEditForm, "Редактирование режимов", true);

        controller.initData(mode);
        Stage stage = controller.getStage();
        stage.setOnCloseRequest(we -> {
            if (we.getEventType() == CustomWindowEvent.SAVE) {
                alert("Успешно сохранено", "Успешно сохранено", Alert.AlertType.INFORMATION);
                refreshModesData();
            }
        });
    }

    @FXML
    private void onAddMode() {
        ModeEditController controller =
                (ModeEditController) runController(modeEditForm, "Редактирование режимов", true);

        controller.initData(null);
        Stage stage = controller.getStage();
        stage.setOnCloseRequest(we -> {
            if (we.getEventType() == CustomWindowEvent.SAVE) {
                alert("Успешно сохранено", "Успешно сохранено", Alert.AlertType.INFORMATION);
                refreshModesData();
            }
        });
    }

    @FXML
    private void onSelectMode() {
        mode = modeTable.getSelectionModel().getSelectedItem();
        if (mode != null) {
            modeDeleteButton.setDisable(false);
        }
    }

    @FXML
    private void onDeleteMode() {
        if (mode != null) {
            modeService.delete(mode);
            modeDeleteButton.setDisable(true);
            refreshModesData();
        }
    }

    private void refreshModesData() {
        ObservableList<Mode> modeData = FXCollections.observableArrayList();
        modeTable.getItems().clear();

        List<Mode> modes = modeService.getList();
        for (Mode mode : modes) {
            modeData.add(mode);
        }
        modeTable.setItems(modeData);
    }
}
