package controllers.Register;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import utils.DatabaseHelper;
import models.User;
import utils.WindowUtil;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.time.LocalDate;

public class RegisterController implements Initializable {

    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private DatePicker dobPicker;
    @FXML private CheckBox rememberMeCheckBox;
    @FXML private Label notifLabel;
    @FXML private TextField usernameField;

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
    public void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        LocalDate dob = dobPicker.getValue();
        String code = "";

        notifLabel.setText("");

        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || dob == null) {
            notifLabel.setText("Semua field harus diisi!");
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

        if (DatabaseHelper.userExists(email)) {
            notifLabel.setText("Email sudah terdaftar!");
            return;
        }

        // Data tambahan (dummy/default)
        String nama = username; // gunakan username dari input
        String bio = "";
        String jenisKelamin = "Laki-laki"; // default, bisa dikembangkan

        // Simpan ke DatabaseHelper
        User user = new User(email, password, nama, dob, phone, code, bio, jenisKelamin);
        DatabaseHelper.addUser(user);
        DatabaseHelper.saveUsersToXML();

        showAlert("Registrasi Berhasil", "Akun berhasil dibuat untuk: " + email);

        // Pindah ke login
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("MediMate - Login");
            WindowUtil.setWindowSize(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka halaman Login.");
        }
    }

    @FXML
    public void handleMasuk(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("MediMate - Login");
            WindowUtil.setWindowSize(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka halaman Login.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
