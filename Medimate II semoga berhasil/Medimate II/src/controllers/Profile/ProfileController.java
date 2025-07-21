package controllers.Profile;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import models.User;
import utils.Session;
import utils.UserStore;

import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalDate;

import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ProfileController implements Initializable {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField dobField;
    @FXML private Label profileNameLabel;
    @FXML private Label profileEmailLabel;
    @FXML private ToggleButton notifToggle;
    @FXML private ImageView profileImageView;
    @FXML private ImageView avatarImageView;
    @FXML private Button ubahFotoButton;

    private boolean notifikasiAktif = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = Session.getCurrentUser();
        if (user != null) {
            if (usernameField != null) usernameField.setText(user.getNama());
            if (emailField != null) emailField.setText(user.getEmail());
            if (phoneField != null) phoneField.setText(user.getNomorHp());
            if (dobField != null && user.getTanggalLahir() != null)
                dobField.setText(user.getTanggalLahir().toString());
            if (profileNameLabel != null) profileNameLabel.setText(user.getNama());
            if (profileEmailLabel != null) profileEmailLabel.setText(user.getEmail());

            // Avatar profil
            String ext = ".png";
            String avatarPath = "src/images/avatar_" + user.getEmail().replaceAll("[^a-zA-Z0-9]", "") + ext;
            File avatarFile = new File(avatarPath);
            File defaultAvatar = new File("src/images/avatar.png");

            if (profileImageView != null) {
                profileImageView.setImage(
                    avatarFile.exists()
                        ? new Image(avatarFile.toURI().toString())
                        : (defaultAvatar.exists() ? new Image(defaultAvatar.toURI().toString()) : null)
                );
            }

            if (avatarImageView != null) {
                avatarImageView.setImage(
                    avatarFile.exists()
                        ? new Image(avatarFile.toURI().toString())
                        : (defaultAvatar.exists() ? new Image(defaultAvatar.toURI().toString()) : null)
                );
            }
        }

        // Toggle notifikasi
        if (notifToggle != null) {
            notifToggle.setSelected(notifikasiAktif);
            updateNotifToggleText();
            notifToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
                notifikasiAktif = newVal;
                updateNotifToggleText();
                new Alert(Alert.AlertType.INFORMATION,
                    notifikasiAktif ? "Notifikasi reminder diaktifkan!" : "Notifikasi reminder dinonaktifkan!"
                ).showAndWait();
            });
        }
    }

    private void updateNotifToggleText() {
        if (notifToggle != null) {
            notifToggle.setText(notifikasiAktif ? "ON" : "OFF");
        }
    }

    @FXML
    private void handleSimpanPerubahan() {
        User user = Session.getCurrentUser();
        if (user != null) {
            try {
                user.setNama(usernameField.getText());
                user.setNomorHp(phoneField.getText());
                user.setTanggalLahir(LocalDate.parse(dobField.getText()));
                // Jika ingin, email juga bisa diubah: user.setEmail(...);

                Session.setCurrentUser(user);  // Simpan ke sesi juga

                boolean success = UserStore.updateUser(user);
                if (success) {
                    if (profileNameLabel != null) profileNameLabel.setText(user.getNama());
                    if (profileEmailLabel != null) profileEmailLabel.setText(user.getEmail());
                    new Alert(Alert.AlertType.INFORMATION, "Perubahan berhasil disimpan.").showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Gagal menyimpan perubahan.").showAndWait();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Data tidak valid: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    private void handleUbahFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());
        if (selectedFile != null) {
            try {
                User user = Session.getCurrentUser();
                String destPath = "src/images/avatar_" + user.getEmail().replaceAll("[^a-zA-Z0-9]", "") + ".png";
                File destFile = new File(destPath);

                // Pastikan folder ada
                File imagesDir = new File("src/images");
                if (!imagesDir.exists()) imagesDir.mkdirs();

                String ext = selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.') + 1).toLowerCase();

                if (!ext.equals("png")) {
                    // Konversi ke PNG
                    Image image = new Image(selectedFile.toURI().toString());
                    javax.imageio.ImageIO.write(
                        javafx.embed.swing.SwingFXUtils.fromFXImage(image, null),
                        "png",
                        destFile
                    );
                } else {
                    try (FileInputStream fis = new FileInputStream(selectedFile);
                         FileOutputStream fos = new FileOutputStream(destFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }

                // Tampilkan gambar baru
                Image newImage = new Image(destFile.toURI().toString());
                if (profileImageView != null) profileImageView.setImage(newImage);
                if (avatarImageView != null) avatarImageView.setImage(newImage);

                new Alert(Alert.AlertType.INFORMATION, "Foto profil berhasil diubah!").showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Gagal mengubah foto: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    private void handleUbahPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profil/ubah_password_dialog.fxml"));
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.setTitle("Ubah Password");
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
