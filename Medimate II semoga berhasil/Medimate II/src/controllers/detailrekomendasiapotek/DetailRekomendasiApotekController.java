package controllers.detailrekomendasiapotek;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import controllers.SideNavigation.SideNavigationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import controllers.Lokasi.LokasiController;
import java.io.IOException;
import javafx.scene.layout.VBox;

public class DetailRekomendasiApotekController {
    @FXML
    public void initialize() {
        // Initialize controller
    }

    @FXML
    private void handleLihatDetail(ActionEvent event) {
        // Handle lihat detail apotek
        System.out.println("Lihat detail apotek clicked");
    }

    @FXML
    private void handleLokasi(ActionEvent event) {
        System.out.println("[DEBUG] handleLokasi dipanggil");
        try {
            Button btn = (Button) event.getSource();
            VBox card = (VBox) btn.getParent();
            Label namaLabel = null;
            Label alamatLabel = null;
            for (Node n : card.getChildren()) {
                if (n instanceof Label && n.getStyleClass().contains("font-content-title") && namaLabel == null) {
                    namaLabel = (Label) n;
                } else if (n instanceof Label && n.getStyleClass().contains("label-subtitle")) {
                    alamatLabel = (Label) n;
                }
            }
            String namaApotek = namaLabel != null ? namaLabel.getText() : "";
            String alamatApotek = alamatLabel != null ? alamatLabel.getText() : "";
            System.out.println("[DEBUG] Nama apotek yang dipilih: " + namaApotek);
            System.out.println("[DEBUG] Alamat apotek yang dipilih: " + alamatApotek);
            String fxmlPath = "/fxml/lokasi/lokasi.fxml";
            java.net.URL lokasiUrl = getClass().getResource(fxmlPath);
            System.out.println("[DEBUG] lokasi.fxml URL: " + lokasiUrl);
            if (lokasiUrl == null) {
                System.out.println("[ERROR] Resource lokasi.fxml tidak ditemukan! Pastikan path dan penempatan file sudah benar di output folder.");
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Resource lokasi.fxml tidak ditemukan!\nPastikan path dan penempatan file sudah benar di output folder.").showAndWait();
                return;
            }
            FXMLLoader loader = new FXMLLoader(lokasiUrl);
            Parent lokasiPane = loader.load();
            System.out.println("[DEBUG] lokasiPane loaded: " + (lokasiPane != null));
            controllers.Lokasi.LokasiController lokasiController = loader.getController();
            lokasiController.setLokasiApotek(namaApotek, alamatApotek);
            AnchorPane mainContentPane = (AnchorPane) btn.getScene().lookup("#mainContentPane");
            System.out.println("[DEBUG] mainContentPane: " + mainContentPane);
            if (mainContentPane != null) {
                mainContentPane.getChildren().setAll(lokasiPane);
                System.out.println("[DEBUG] Halaman lokasi berhasil dimuat ke main content.");
            } else {
                System.out.println("[DEBUG] mainContentPane masih null!");
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] Exception saat membuka halaman lokasi:");
            e.printStackTrace();
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Gagal membuka halaman lokasi.\n" + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        System.out.println("[DEBUG] Tombol Back diklik di halaman detail rekomendasi apotek");
        try {
            String fxmlPath = "/fxml/dashboard/dashboard_content.fxml";
            java.net.URL dashboardUrl = getClass().getResource(fxmlPath);
            System.out.println("[DEBUG] dashboard_content.fxml URL: " + dashboardUrl);
            if (dashboardUrl == null) {
                System.out.println("[ERROR] Resource dashboard_content.fxml tidak ditemukan!");
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Resource dashboard_content.fxml tidak ditemukan!").showAndWait();
                return;
            }
            Parent dashboardPane = FXMLLoader.load(dashboardUrl);
            Button btn = (Button) event.getSource();
            AnchorPane mainContentPane = (AnchorPane) btn.getScene().lookup("#mainContentPane");
            System.out.println("[DEBUG] mainContentPane: " + mainContentPane);
            if (mainContentPane != null) {
                mainContentPane.getChildren().setAll(dashboardPane);
                System.out.println("[DEBUG] Halaman dashboard berhasil dimuat kembali.");
            } else {
                System.out.println("[DEBUG] mainContentPane masih null!");
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] Exception saat kembali ke halaman dashboard:");
            e.printStackTrace();
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Gagal kembali ke halaman dashboard.\n" + e.getMessage()).showAndWait();
        }
    }
} 