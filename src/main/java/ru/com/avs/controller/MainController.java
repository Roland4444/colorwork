package ru.com.avs.controller;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.opencv.core.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import ru.com.avs.model.Mode;
import ru.com.avs.model.Property;
import ru.com.avs.model.Scale;
import ru.com.avs.model.ThreadModel;
import ru.com.avs.service.AuthService;
import ru.com.avs.service.ModeService;
import ru.com.avs.service.PropertyService;
import ru.com.avs.service.ScaleService;
import ru.com.avs.service.ThreadService;
import ru.com.avs.thread.CameraThread;
import ru.com.avs.thread.ExportThread;
import ru.com.avs.thread.ScaleThread;

@Component("MainController")
public class MainController extends AbstractController {

    @FXML
    private Button adminButton;
    @FXML
    private Button settingButton;
    @FXML
    private Button logoutButton;
    @FXML
    private ButtonBar modeBox;
    @FXML
    private AnchorPane scalePane;

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private AuthService authService;
    @Autowired
    private ModeService modeService;
    @Autowired
    private ScaleService scaleService;
    @Autowired
    private ThreadService threadService;

    private ExportThread exportThread;

    /**
     * Initialize construct.
     */
    public void initialize() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int activeMode = propertyService.getIntProperty("mode");

        List<Mode> modeList = modeService.getList();
        ToggleGroup group = new ToggleGroup();
        for (Mode mode : modeList) {
            ToggleButton toggleButton = new ToggleButton();
            toggleButton.setText(mode.getName());
            toggleButton.setId(String.valueOf(mode.getId()));
            toggleButton.setToggleGroup(group);
            toggleButton.setPrefWidth(400);
            ButtonBar.setButtonData(toggleButton, ButtonBar.ButtonData.LEFT);

            if (activeMode == mode.getId()) {
                toggleButton.setSelected(true);
                startView(mode.getId());
            }
            modeBox.getButtons().add(toggleButton);
            setToggleListener(toggleButton);
        }

        exportThread = (ExportThread) context.getBean("ExportThread");
        exportThread.start();
    }

    private void setToggleListener(ToggleButton toggleButton) {
        toggleButton.setOnAction(event -> {
            if (toggleButton.isSelected()) {
                Property property = propertyService.findByName("mode");
                property.setValue(toggleButton.getId());
                propertyService.update(property);
                startView(Integer.parseInt(toggleButton.getId()));
            }
        });
    }

    private void startView(int id) {
        threadService.stopThreads();
        scalePane.getChildren().clear();
        List<Scale> scales = scaleService.getByMode(id);

        double x = 10;
        if (scales.size() == 1) {
            x = 200;
        }

        for (Scale scale : scales) {
            AnchorPane anchorPane = createAnchor(scale, x, 10);
            scalePane.getChildren().add(anchorPane);
            x += 390;
        }

        if (this.getStage() != null && x > 700) {
            this.getStage().setMinWidth(x + 20);
            this.getStage().setWidth(x + 20);
        } else if (this.getStage() != null && x < 700) {
            this.getStage().setMinWidth(810);
            this.getStage().setWidth(810);
        }
    }

    private AnchorPane createAnchor(Scale scale, double x, double y) {
        Label label = new Label();
        label.setLayoutX(100);
        label.setLayoutY(14);
        label.getStyleClass().add("main-weight-text");
        label.setText(scale.getName());

        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(label);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(350);
        imageView.setFitHeight(350);
        imageView.setLayoutX(13);
        imageView.setLayoutY(57);
        imageView.setImage(new Image(String.valueOf(ClassLoader.getSystemResource("icon/loading.gif"))));
        pane.getChildren().add(imageView);

        CameraThread cameraThread1 =
                (CameraThread) context.getBean("CameraThread", imageView, scale.getCamera());
        cameraThread1.start();

        threadService.addThread(new ThreadModel(cameraThread1, ThreadModel.CAMERA_THREAD, scale.getId()));

        Label label2 = new Label();
        label2.setLayoutX(91);
        label2.setLayoutY(419);
        label2.setText("Текущий вес: ");
        label2.getStyleClass().add("main-weight-text");
        pane.getChildren().add(label2);

        Label scaleWeight = new Label();
        scaleWeight.setLayoutX(135);
        scaleWeight.setLayoutY(454);
        scaleWeight.setText("0.0");
        scaleWeight.getStyleClass().add("main-weight-value");
        pane.getChildren().add(scaleWeight);

        ScaleThread scaleThread = (ScaleThread) context.getBean("ScaleThread", scaleWeight, scale);
        scaleThread.start();
        threadService.addThread(new ThreadModel(scaleThread, ThreadModel.SCALE_THREAD, scale.getId()));

        pane.prefHeight(545);
        pane.prefWidth(380);
        pane.setLayoutX(x);
        pane.setLayoutY(y);
        pane.setId(String.valueOf(scale.getId()));

        return pane;
    }

    @FXML
    private void exit() {
        this.getStage().fireEvent(
                new WindowEvent(
                        this.getStage(),
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
    }

    @FXML
    private void openSettings() {
        runController("settingsForm", "Настройки", true);
    }

    @FXML
    private void login() {
        AuthController controller =
                (AuthController) runController("auth", "Авторизация", true);

        Stage stage = controller.getStage();
        stage.setOnCloseRequest(we -> {
            enableAdminMode(authService.isAdminMode());
        });
    }

    @FXML
    private void openWaybillJournal() {
        runController("waybillJournal", "Журнал сделок", true, true, true);
    }

    @FXML
    private void logout() {
        authService.logout();
        enableAdminMode(authService.isAdminMode());

    }

    private void enableAdminMode(boolean enable) {
        adminButton.setVisible(!enable);
        adminButton.setManaged(!enable);
        settingButton.setVisible(enable);
        settingButton.setManaged(enable);
        logoutButton.setVisible(enable);
        logoutButton.setManaged(enable);
    }
}
