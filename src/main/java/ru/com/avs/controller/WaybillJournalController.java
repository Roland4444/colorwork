package ru.com.avs.controller;

import static ru.com.avs.util.UserUtils.fileExists;
import static ru.com.avs.util.UserUtils.getImage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.com.avs.model.Metal;
import ru.com.avs.model.Mode;
import ru.com.avs.model.Tare;
import ru.com.avs.model.ViewModel;
import ru.com.avs.model.Waybill;
import ru.com.avs.model.Weighing;
import ru.com.avs.service.AuthService;
import ru.com.avs.service.MetalService;
import ru.com.avs.service.ModeService;
import ru.com.avs.service.PropertyService;
import ru.com.avs.service.WaybillService;
import ru.com.avs.service.WeighingService;

@Component("WaybillJournalController")
public class WaybillJournalController extends AbstractController {

    public TableColumn<ViewModel, LocalDate> dateColumn;
    public TableColumn<ViewModel, LocalTime> timeColumn;
    public TableColumn<ViewModel, Integer> waybillColumn;
    public TableColumn<ViewModel, Metal> metalColumn;
    public TableColumn<ViewModel, String> commentColumn;
    public TableColumn<ViewModel, String> bruttoColumn;
    public TableColumn<ViewModel, Tare> tareColumn;
    public TableColumn<ViewModel, String> nettoColumn;
    public TableColumn<ViewModel, String> cloggingColumn;
    public TableColumn<ViewModel, String> trashColumn;
    public TableColumn<ViewModel, Boolean> modeColumn;
    public TableColumn<ViewModel, Boolean> completeColumn;
    public TableColumn<ViewModel, String> stateColumn;

    private ViewModel viewModel;
    @FXML
    private TableView<ViewModel> waybillTable;
    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private TextField commentFilter;
    @FXML
    private ImageView preview;

    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button exportButton;
    @FXML
    private Button printButton;

    @Autowired
    private PropertyService property;

    @Autowired
    private AuthService authService;

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private WeighingService weighingService;

    @Autowired
    private MetalService metalService;

    @Autowired
    private ModeService modeService;

    /**
     * init.
     */
    public void initialize() {
        dateStart.setValue(LocalDate.now());
        dateEnd.setValue(LocalDate.now());
        adminMode(authService.isAdminMode());

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timeCreate"));
        waybillColumn.setCellValueFactory(new PropertyValueFactory<>("waybill"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        metalColumn.setCellValueFactory(new PropertyValueFactory<>("metal"));
        bruttoColumn.setCellValueFactory(new PropertyValueFactory<>("brutto"));
        tareColumn.setCellValueFactory(new PropertyValueFactory<>("tare"));
        nettoColumn.setCellValueFactory(new PropertyValueFactory<>("netto"));
        cloggingColumn.setCellValueFactory(new PropertyValueFactory<>("clogging"));
        trashColumn.setCellValueFactory(new PropertyValueFactory<>("trash"));
        modeColumn.setCellValueFactory(new PropertyValueFactory<>("mode"));
        completeColumn.setCellValueFactory(new PropertyValueFactory<>("complete"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("upload"));

        refreshData();
        waybillTable.setEditable(true);
    }

    @FXML
    private void isSelect() {
        viewModel = waybillTable.getSelectionModel().getSelectedItem();
        if (viewModel != null) {
            String imgPath = property.getProperty("image.path")
                    + viewModel.getDateCreate() + "/previewOur-" + viewModel.getWeighingId() + ".jpg";

            if (!fileExists(imgPath)) {
                imgPath = property.getProperty("image.path")
                        + viewModel.getDateCreate() + "/preview-" + viewModel.getWeighingId() + ".jpg";
            }
            Image image = getImage(imgPath);
            preview.setImage(image);
            deactivateButtons(false);
        }
    }

    @FXML
    private void search() {
        refreshData();
    }

    @FXML
    private void help(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message Here...");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("I have a great message for you!");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    };

    @FXML
    private void edit() {
        if (viewModel != null) {
            WaybillEditController controller =
                    (WaybillEditController) runController("waybillEditForm", "Редактирование сделки", true);
            controller.initData(viewModel);
            Stage stage = controller.getStage();
            stage.setOnCloseRequest(we -> {
                refreshData();
                deactivateButtons(true);
            });
        } else {
            alert("Не выбрано взвешивание", "Ошибка", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void delete() {

        if (viewModel != null) {
            Optional<ButtonType> option = dialog("Удалить выбранное взвешивание?",
                    "Удаление", Alert.AlertType.CONFIRMATION);

            if (option.get() == ButtonType.OK) {
                Weighing weighing = weighingService.getById(viewModel.getWeighingId());

                if (weighing.getWaybill().getWeighings().size() == 1) {
                    waybillService.delete(weighing.getWaybill());
                } else {
                    weighingService.delete(weighing);
                }
                refreshData();
            }
            deactivateButtons(true);
        } else {
            alert("Не выбрано взвешивание", "Ошибка", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void export() {
        Waybill waybill = waybillService.getById(viewModel.getWaybillId());
        if (waybill.isComplete()) {
            if (waybillService.exportWaybill(waybill)) {
                alert("Экспортировано", "Ок", Alert.AlertType.INFORMATION);
            } else {
                alert("Произошла ошибка", "Ошибка", Alert.AlertType.ERROR);
            }
        } else {
            alert("Невозможно экспортировать\n" +
                    "Сделка еще не завершена!", "Ошибка", Alert.AlertType.ERROR);
        }
        deactivateButtons(true);
    }

    @FXML
    private void print() {
        Waybill waybill = waybillService.getById(viewModel.getWaybillId());
        if (waybill.isComplete()) {
            PrintController controller =
                    (PrintController) runController("printForm", "Чек", true);

            controller.initData(waybill, false);
            Stage stage = controller.getStage();
            stage.setOnCloseRequest(we -> {
                deactivateButtons(true);
            });
        } else {
            alert("Невозможно напечатаь чек\n" +
                    "Сделка еще не завершена!", "Ошибка", Alert.AlertType.ERROR);
        }
    }

    private void adminMode(boolean enable) {
        editButton.setVisible(enable);
        deleteButton.setVisible(enable);
        deactivateButtons(true);
    }

    private void deactivateButtons(boolean activate) {
        editButton.setDisable(activate);
        deleteButton.setDisable(activate);
        exportButton.setDisable(activate);
        printButton.setDisable(activate);
    }

    private void refreshData() {
        ObservableList<ViewModel> data = FXCollections.observableArrayList();
        waybillTable.getItems().clear();
        List<Waybill> waybills =
                waybillService.search(dateStart.getValue(), dateEnd.getValue(), commentFilter.getText());
        List<ViewModel> viewModels = createModel(waybills);
        for (ViewModel viewModel : viewModels) {
            data.add(viewModel);
        }
        waybillTable.setItems(data);
    }

    private List<ViewModel> createModel(List<Waybill> waybills) {
        List<ViewModel> viewModels = new ArrayList<>();
        for (Waybill waybill : waybills) {

            List<Weighing> weighings = waybill.getWeighings();

            List<Mode> modes = modeService.getList();
            for (Weighing weighing : weighings) {
                ViewModel viewModel = new ViewModel();
                viewModel.setWaybillId(waybill.getId());
                viewModel.setDateCreate(waybill.getDateCreate());
                viewModel.setTimeCreate(waybill.getTimeCreate());
                viewModel.setWaybill(waybill.getWaybill());
                Metal metal = metalService.find(weighing.getMetal());
                viewModel.setMetal(metal);
                viewModel.setComment(waybill.getComment());
                viewModel.setUpload(waybill.isUpload() ? "Выгружен" : "Не выгружен");
                viewModel.setComplete(waybill.isComplete() ? "Да" : "Нет");

                Mode mode = modeService.getByCode(modes, waybill.getMode());
                viewModel.setMode(mode.getName());
                viewModel.setWeighingId(weighing.getId());
                viewModel.setBrutto(weighing.getBrutto());
                viewModel.setNetto(weighing.getNetto());
                viewModel.setTrash(weighing.getTrash());
                viewModel.setClogging(weighing.getClogging());
                viewModel.setTare(weighing.getTare());
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }
}
