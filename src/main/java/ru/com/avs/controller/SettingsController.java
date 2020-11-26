package ru.com.avs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.com.avs.service.AuthService;

@Component("SettingsController")
public class SettingsController extends AbstractController {

    @FXML
    private PasswordField currentPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField newPasswordRetry;

    @Autowired
    private AuthService authService;

    @FXML
    private void savePassword() {
        if (authService.isSuccessAuth(currentPassword.getText())) {

            if (!newPassword.getText().equals("") && newPassword.getText().equals(newPasswordRetry.getText())) {
                authService.updatePassword(newPassword.getText());
                currentPassword.clear();
                newPassword.clear();
                newPasswordRetry.clear();
                alert("Успешно изменен", "Успех", Alert.AlertType.INFORMATION);
            } else {
                alert("Пароли не совпадают", "Ошибка", Alert.AlertType.ERROR);
            }
        } else {
            alert("Неверно введен текущий пароль", "Ошибка", Alert.AlertType.ERROR);
        }
    }
}
