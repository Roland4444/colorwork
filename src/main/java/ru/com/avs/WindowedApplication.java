package ru.com.avs;

import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Properties;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.com.avs.config.ShutdownBaseBean;
import ru.com.avs.controller.MainController;
import ru.com.avs.thread.HttpServerThread;
import ru.com.avs.util.SpringLoader;

public class WindowedApplication extends Application {

    private static final Logger log = LoggerFactory.getLogger(WindowedApplication.class);
    private static final int PORT = 9991;

    /**
     * Run database migrations.
     */
    private static void runDatabaseMigrations() {
        String host = null;

        try (InputStream input =
                     WindowedApplication.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties property = new Properties();
            property.load(input);
            host = property.getProperty("jdbc.url") + ";create=true";

        } catch (IOException e) {
            log.error("IOException:", e.getMessage());
        }

        Flyway.configure()
                .dataSource(host, null, null)
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }

    /**
     * Main.
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        checkIfRunning();
        runDatabaseMigrations();
        launch(args);
    }

    private static void checkIfRunning() {

        try {
            ServerSocket socket = new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[]{127, 0, 0, 1}));
        } catch (BindException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Другой экземпляр приложения уже запущен!",
                    "Ошибка запуска", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (IOException e) {
            log.error("IOException:", e.getMessage());
            System.exit(2);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        Stage stageMain = new Stage();
        stageMain.setTitle("Весовая");
        MainController controller = (MainController) SpringLoader.loadControllerFxml("/fxml/main.fxml");
        Scene scene = new Scene((Parent) controller.getView());
        scene.getStylesheets().add((getClass().getResource("/css/mainStyle.css")).toExternalForm());
        stageMain.setScene(scene);
        stageMain.getIcons().add(new Image(String.valueOf(ClassLoader.getSystemResource("icon/icon.png"))));
        //stageMain.resizableProperty().setValue(Boolean.FALSE);
        stageMain.setMinWidth(810);
        stageMain.setMinHeight(700);
        controller.setStage(stageMain);

        stageMain.setOnCloseRequest(event -> {
                    ShutdownBaseBean bean = (ShutdownBaseBean) SpringLoader.getBean(ShutdownBaseBean.class);
                    bean.shutdown();
                    System.exit(0);
                }
        );
        stageMain.show();
        new HttpServerThread().start();
    }
}
