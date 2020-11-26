package ru.com.avs.controller;

import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.com.avs.api.ApiService;
import ru.com.avs.model.Metal;
import ru.com.avs.service.MetalService;

@Component
public class MetalTabController extends AbstractController {

    @FXML
    private TableColumn<Metal, Integer> idColumn;
    @FXML
    private TableColumn<Metal, String> nameColumn;
    @FXML
    private TableView<Metal> table;

    @Autowired
    private ApiService apiService;

    @Autowired
    private MetalService departmentService;

    /**
     * Initialize.
     */
    public void initialize() {
        refreshDepartmentsData();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.setEditable(true);
    }

    private void refreshDepartmentsData() {
        ObservableList<Metal> metalsData = FXCollections.observableArrayList();
        table.getItems().clear();
        List<Metal> metals = departmentService.getAll();
        metalsData.addAll(metals);
        table.setItems(metalsData);
    }

    /**
     * Sync departments.
     * @param actionEvent event
     */
    public void onSync(ActionEvent actionEvent) {
        List<Map<String, Object>> metals = apiService.getMetals();
        if (metals.size() > 0) {
            departmentService.update(metals);
        }
        refreshDepartmentsData();
    }
}
