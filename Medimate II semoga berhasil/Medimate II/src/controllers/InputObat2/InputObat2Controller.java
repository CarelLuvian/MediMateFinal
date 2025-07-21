package controllers.InputObat2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import models.RincianObat;
import utils.RincianObatHelper;
import controllers.InputObat1.InputObat1Controller;
import java.time.LocalDate;
import java.util.List;
import utils.Session;
import models.User;

public class InputObat2Controller {
    @FXML private DatePicker tanggalMulaiPicker;
    @FXML private DatePicker tanggalSelesaiPicker;
    @FXML private TextField waktuMinumField;
    @FXML private javafx.scene.control.ComboBox<String> reminderComboBox;
    @FXML private TextArea catatanArea;

    @FXML
    public void initialize() {
        if (reminderComboBox != null) {
            reminderComboBox.getItems().addAll("5 menit sebelum", "10 menit sebelum", "15 menit sebelum", "30 menit sebelum");
        }
    }

    @FXML
    private void handlePresetWaktu(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            String jam = ((Button) event.getSource()).getText();
            if (waktuMinumField != null) {
                waktuMinumField.setText(jam);
            }
        }
    }

    @FXML
    private void handleSimpan(ActionEvent event) {
        try {
            // Gabungkan data input obat 1 & 2
            String namaObat = controllers.InputObat1.InputObat1Controller.tempObat1.getOrDefault("namaObat", "");
            String kategori = controllers.InputObat1.InputObat1Controller.tempObat1.getOrDefault("kategori", "");
            String dosis = controllers.InputObat1.InputObat1Controller.tempObat1.getOrDefault("dosis", "");
            String frekuensi = controllers.InputObat1.InputObat1Controller.tempObat1.getOrDefault("frekuensi", "");
            LocalDate tglMulai = tanggalMulaiPicker.getValue();
            LocalDate tglSelesai = tanggalSelesaiPicker.getValue();
            String waktuMinum = waktuMinumField.getText();
            String reminder = reminderComboBox.getValue();
            String catatan = catatanArea.getText();
            RincianObat rincian = new RincianObat(namaObat, kategori, dosis, frekuensi, tglMulai, tglSelesai, waktuMinum, reminder, catatan);
            // Set email user login
            User currentUser = Session.getCurrentUser();
            if (currentUser != null) {
                rincian.setEmail(currentUser.getEmail());
            }
            // Simpan ke XML
            java.util.List<RincianObat> list = utils.RincianObatHelper.loadAll();
            list.add(rincian);
            utils.RincianObatHelper.saveAll(list);
            // Redirect ke kalender obat
            javafx.scene.Parent kalenderObatPage = javafx.fxml.FXMLLoader.load(getClass().getResource("/fxml/kalender/kalender_content.fxml"));
            controllers.SideNavigation.SideNavigationController.staticMainContent.getChildren().setAll((javafx.scene.Node) kalenderObatPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 