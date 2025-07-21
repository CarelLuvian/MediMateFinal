package controllers.Dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import models.Lokasi;
import javafx.scene.control.Alert;

public class ApotekTerdekatController {

    @FXML
    private Label labelApotek1, labelSub1, labelApotek2, labelSub2;
    @FXML
    private ImageView iconClock1, iconClock2;
    @FXML
    private Button btnLokasi1, btnLokasi2;

    private Lokasi lokasi1, lokasi2;

    public void initialize() {
        System.out.println("Init ApotekTerdekatController");
        lokasi1 = new Lokasi("Pharmacies UII Farma", "Jl. Kaliurang KM 14, Yogyakarta", -7.747033, 110.355398);
        lokasi2 = new Lokasi("Apotek K-24 Besi Sleman", "Jl. Magelang KM 10, Sleman", -7.715556, 110.338056);

        // Simulasi lokasi pengguna
        String userLocation = "Yogyakarta"; // <-- Ganti dengan API lokasi jika ingin real-time

        // Simulasi data apotek berdasarkan lokasi
        if (userLocation.equalsIgnoreCase("Yogyakarta")) {
            labelApotek1.setText("Pharmacies UII Farma");
            labelSub1.setText("Terdekat dari Anda");
            labelApotek2.setText("Apotek K-24 Besi Sleman");
            labelSub2.setText("Terdekat dari Anda");

            // Icon
            iconClock1.setImage(new Image(getClass().getResourceAsStream("/images/clock-icon.png")));
            iconClock2.setImage(new Image(getClass().getResourceAsStream("/images/clock-icon.png")));

            // Set up location buttons
            btnLokasi1.setOnAction(e -> bukaLokasi(lokasi1));
            btnLokasi2.setOnAction(e -> bukaLokasi(lokasi2));
        }
    }

    private void bukaLokasi(Lokasi lokasi) {
        try {
            String mapsUrl = String.format("https://www.google.com/maps/search/?api=1&query=%.6f,%.6f", 
                lokasi.getLatitude(), lokasi.getLongitude());
            java.awt.Desktop.getDesktop().browse(new java.net.URI(mapsUrl));
        } catch (Exception e) {
            System.out.println("Error membuka lokasi: " + e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Gagal membuka lokasi di Google Maps").showAndWait();
        }
    }

    private void showNotAvailable() {
        new Alert(Alert.AlertType.INFORMATION, "Fitur ini belum tersedia.").showAndWait();
    }

    @FXML
    private void handleLihatSaya(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detailrekomendasiobat/detailrekomendasiobat.fxml"));
            Parent detailPage = loader.load();
            // Get current stage
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(detailPage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLihatSemua() {
        System.out.println("[DEBUG] handleLihatSemua dipanggil");
        try {
            String fxmlPath = "/fxml/detailrekomendasiapotek/detailrekomendasiapotek.fxml";
            System.out.println("[DEBUG] Path FXML: " + fxmlPath);
            Parent detail = javafx.fxml.FXMLLoader.load(getClass().getResource(fxmlPath));
            System.out.println("[DEBUG] FXML loaded: " + (detail != null));
            System.out.println("[DEBUG] staticMainContent: " + controllers.SideNavigation.SideNavigationController.staticMainContent);
            if (controllers.SideNavigation.SideNavigationController.staticMainContent != null) {
                controllers.SideNavigation.SideNavigationController.staticMainContent.getChildren().setAll(detail);
                System.out.println("[DEBUG] Halaman detail rekomendasi apotek berhasil dimuat ke main content.");
            } else {
                System.out.println("[DEBUG] staticMainContent masih null!");
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] Exception saat membuka halaman detail rekomendasi apotek:");
            e.printStackTrace();
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Gagal membuka halaman detail rekomendasi apotek.").showAndWait();
        }
    }
}
