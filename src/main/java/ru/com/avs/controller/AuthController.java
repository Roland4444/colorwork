package ru.com.avs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.com.avs.service.AuthService;

@Component("AuthController")
public class AuthController extends AbstractController {

    @FXML
    private TextField password;

    @Autowired
    private AuthService authService;

    @FXML
    private void login() {

        if (!authService.isSuccessAuth(password.getText())) {
            authService.setAdminMode(true);
            this.getStage().getOnCloseRequest()
                    .handle(new WindowEvent(this.getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
            closeThisWindow();
        } else {
            alert("Неверно введен пароль", "Авторизация не пройдена", Alert.AlertType.ERROR);
        }
    }
}
