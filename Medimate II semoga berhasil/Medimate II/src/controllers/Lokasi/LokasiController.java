package controllers.Lokasi;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class LokasiController {
    @FXML
    private Label namaApotekLabel;
    @FXML
    private WebView mapsWebView;

    private String namaApotek;

    public void setNamaApotek(String namaApotek) {
        this.namaApotek = namaApotek;
        namaApotekLabel.setText(namaApotek);
        // Tampilkan Google Maps dengan query nama apotek
        String mapsUrl = "https://www.google.com/maps/search/?api=1&query=" + namaApotek.replace(" ", "+");
        mapsWebView.getEngine().load(mapsUrl);
    }

    public void setLokasiApotek(String namaApotek, String alamatApotek) {
        namaApotekLabel.setText(namaApotek);
        // Query Google Maps dengan nama + alamat
        String query = namaApotek + ", " + alamatApotek;
        String mapsUrl = "https://www.google.com/maps/search/?api=1&query=" + query.replace(" ", "+");
        mapsWebView.getEngine().load(mapsUrl);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        System.out.println("[DEBUG] Tombol Back diklik di halaman lokasi");
        try {
            String fxmlPath = "/fxml/detailrekomendasiapotek/detailrekomendasiapotek.fxml";
            java.net.URL detailUrl = getClass().getResource(fxmlPath);
            System.out.println("[DEBUG] detailrekomendasiapotek.fxml URL: " + detailUrl);
            if (detailUrl == null) {
                System.out.println("[ERROR] Resource detailrekomendasiapotek.fxml tidak ditemukan!");
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Resource detailrekomendasiapotek.fxml tidak ditemukan!").showAndWait();
                return;
            }
            Parent detailPane = FXMLLoader.load(detailUrl);
            // Cari mainContentPane dari scene
            Button btn = (Button) event.getSource();
            AnchorPane mainContentPane = (AnchorPane) btn.getScene().lookup("#mainContentPane");
            System.out.println("[DEBUG] mainContentPane: " + mainContentPane);
            if (mainContentPane != null) {
                mainContentPane.getChildren().setAll(detailPane);
                System.out.println("[DEBUG] Halaman detail rekomendasi apotek berhasil dimuat kembali.");
            } else {
                System.out.println("[DEBUG] mainContentPane masih null!");
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] Exception saat kembali ke halaman detail rekomendasi apotek:");
            e.printStackTrace();
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Gagal kembali ke halaman detail rekomendasi apotek.\n" + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void initialize() {
        // Default kosong, namaApotek di-set setelah navigasi
    }
} 