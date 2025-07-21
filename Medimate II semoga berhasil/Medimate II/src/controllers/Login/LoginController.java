package controllers.Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import utils.DatabaseHelper;
import models.User;
import utils.WindowUtil;
import javafx.fxml.Initializable;

import java.io.IOException;

public class LoginController implements Initializable {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label notifLabel;
    @FXML private CheckBox rememberMeCheckBox;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        if (utils.Session.getCurrentUser() != null) {
            // Sudah login, langsung ke dashboard
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard/dashboard.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Dashboard - MediMate");
                WindowUtil.setWindowSize(stage);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        notifLabel.setText("");

        if (email.isEmpty() || password.isEmpty()) {
            notifLabel.setText("Email dan password harus diisi!");
            return;
        }

        if (!email.matches("^\\S+@\\S+\\.\\S+$")) {
            notifLabel.setText("Format email tidak valid!");
            return;
        }

        if (password.length() < 6) {
            notifLabel.setText("Password minimal 6 karakter!");
            return;
        }

        User user = DatabaseHelper.getUser(email);
        if (user != null && user.getPassword().equals(password)) {
            notifLabel.setText("");
            utils.Session.setCurrentUser(user);
            System.out.println("[DEBUG] User login: " + user.getNama());
            System.out.println("[DEBUG] Session user: " + utils.Session.getCurrentUser().getNama());
            showAlert("Login Berhasil", "Selamat datang, " + user.getNama() + "!");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard/dashboard.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Dashboard - MediMate");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Gagal membuka halaman Dashboard.");
            }
        } else {
            notifLabel.setText("Email atau password salah!");
        }
    }

    @FXML
    public void handleDaftar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register/register.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Daftar - MediMate");
            WindowUtil.setWindowSize(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka halaman Daftar.");
        }
    }

    @FXML
    public void handleForgotPassword() {
        showAlert("Navigasi", "Pindah ke halaman Lupa Password");
        // TODO: Navigasi ke reset_password.fxml
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
