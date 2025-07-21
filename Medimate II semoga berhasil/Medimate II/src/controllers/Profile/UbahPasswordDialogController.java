package controllers.Profile;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.User;
import utils.Session;
import utils.UserStore;

public class UbahPasswordDialogController {
    @FXML private PasswordField oldPassField;
    @FXML private PasswordField newPassField;
    @FXML private PasswordField confirmPassField;

    @FXML
    private void handleSimpan() {
        User user = Session.getCurrentUser();
        if (user == null) return;
        String oldPass = oldPassField.getText();
        String newPass = newPassField.getText();
        String confirmPass = confirmPassField.getText();
        if (!user.getPassword().equals(oldPass)) {
            new Alert(Alert.AlertType.ERROR, "Password lama salah!").showAndWait();
            return;
        }
        if (newPass.length() < 6) {
            new Alert(Alert.AlertType.ERROR, "Password baru minimal 6 karakter!").showAndWait();
            return;
        }
        if (!newPass.equals(confirmPass)) {
            new Alert(Alert.AlertType.ERROR, "Konfirmasi password tidak cocok!").showAndWait();
            return;
        }
        user.setPassword(newPass);
        Session.setCurrentUser(user);
        boolean success = UserStore.updateUser(user);
        if (success) {
            new Alert(Alert.AlertType.INFORMATION, "Password berhasil diubah!").showAndWait();
            closeDialog();
        } else {
            new Alert(Alert.AlertType.ERROR, "Gagal mengubah password!").showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) oldPassField.getScene().getWindow();
        stage.close();
    }
} 