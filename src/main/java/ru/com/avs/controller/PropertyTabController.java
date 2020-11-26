package ru.com.avs.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.com.avs.model.Property;
import ru.com.avs.service.PropertyService;

@Component
public class PropertyTabController extends AbstractController {

    @FXML
    private TableColumn<Property, Integer> idColumn;
    @FXML
    private TableColumn<Property, String> nameColumn;
    @FXML
    private TableColumn<Property, String> valueColumn;
    @FXML
    private TableView<Property> propertyTable;

    @Autowired
    private PropertyService propertyService;

    private String propertyEditForm = "settings/propertyEditForm";

    /**
     * Initialize.
     */
    public void initialize() {
        refreshPropertyData();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        propertyTable.setEditable(true);
    }

    @FXML
    private void onPropertyEditStart() {
        Property property = propertyTable.getSelectionModel().getSelectedItem();
        PropertyEditController controller =
                (PropertyEditController) runController(propertyEditForm, "Редактирование настроек", true);

        controller.initData(property);
        Stage stage = controller.getStage();
        stage.setOnCloseRequest(we -> {
            refreshPropertyData();
        });
    }

    private void refreshPropertyData() {
        ObservableList<Property> propertyData = FXCollections.observableArrayList();
        propertyTable.getItems().clear();

        List<Property> properties = propertyService.findAll();
        for (Property property : properties) {
            propertyData.add(property);
        }
        propertyTable.setItems(propertyData);
    }
}
