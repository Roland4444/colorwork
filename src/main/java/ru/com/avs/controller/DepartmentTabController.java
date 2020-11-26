package ru.com.avs.controller;

import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.com.avs.api.ApiService;
import ru.com.avs.model.Department;
import ru.com.avs.model.Property;
import ru.com.avs.service.DepartmentService;
import ru.com.avs.service.PropertyService;

@Component
public class DepartmentTabController extends AbstractController {

    @FXML
    private TableColumn<Department, Integer> departmentIdColumn;
    @FXML
    private TableColumn<Department, String> departmentNameColumn;
    @FXML
    private TableColumn<Department, String> departmentAliasColumn;
    @FXML
    private TableColumn<Department, String> departmentTypeColumn;
    @FXML
    private TableView<Department> departmentTable;
    @FXML
    private Label lblSelected;

    @Autowired
    private ApiService apiService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PropertyService propertyService;

    /**
     * Initialize.
     */
    public void initialize() {
        refreshDepartmentsData();
        departmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        departmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        departmentAliasColumn.setCellValueFactory(new PropertyValueFactory<>("alias"));
        departmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        departmentTable.setEditable(true);
    }

    private void refreshDepartmentsData() {
        ObservableList<Department> departmentsData = FXCollections.observableArrayList();
        departmentTable.getItems().clear();
        List<Department> departments = departmentService.getAll();
        departmentsData.addAll(departments);
        departmentTable.setItems(departmentsData);

        int id = Integer.parseInt(propertyService.getProperty("department.id"));
        for (Department department : departments) {
            if (department.getId() == id) {
                lblSelected.setText(department.getName());
            }
        }
    }

    /**
     * Sync departments.
     * @param actionEvent event
     */
    public void onSync(ActionEvent actionEvent) {
        List<Map<String, Object>> remoteDepartments = apiService.getDepartments();
        if (remoteDepartments.size() > 0) {
            departmentService.update(remoteDepartments);
        }
        refreshDepartmentsData();
    }

    /**
     * Save selected department to property.
     * @param actionEvent event
     */
    public void onSelect(ActionEvent actionEvent) {
        Department department = departmentTable.getSelectionModel().getSelectedItem();
        if (department != null) {
            Property property = propertyService.findByName("department.id");
            property.setValue(String.valueOf(department.getId()));
            propertyService.update(property);
            lblSelected.setText(department.getName());
        } else {
            alert("Не выбрано подразделение", "Ошибка", Alert.AlertType.ERROR);
        }
    }
}
