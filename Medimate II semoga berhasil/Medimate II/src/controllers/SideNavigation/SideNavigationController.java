package controllers.SideNavigation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import utils.WindowUtil;
import utils.Session;
import javafx.scene.control.Label;

/**
 * Controller untuk menu navigasi samping (Side Navigation).
 */
public class SideNavigationController implements Initializable {

    // AnchorPane utama tempat konten akan dimuat (ditetapkan dari DashboardController)
    public static AnchorPane staticMainContent;

    @FXML private HBox menuDashboard;
    @FXML private HBox menuCalendar;
    // Hapus menuVisualisasi karena sudah tidak ada di FXML
    @FXML private HBox menuProfile;
    @FXML private HBox menuLogout;

    private void setActiveMenu(HBox selectedMenu) {
        menuDashboard.getStyleClass().removeAll("sidebar-menu-active");
        menuCalendar.getStyleClass().removeAll("sidebar-menu-active");
        // Hapus menuVisualisasi dari pengelolaan style
        menuProfile.getStyleClass().removeAll("sidebar-menu-active");
        menuLogout.getStyleClass().removeAll("sidebar-menu-active");
        if (!selectedMenu.getStyleClass().contains("sidebar-menu-active")) {
            selectedMenu.getStyleClass().add("sidebar-menu-active");
        }
    }

    /**
     * Metode setter agar controller ini bisa diberikan referensi ke kontainer utama (dari luar).
     */
    public void setMainContent(AnchorPane mainContent) {
        staticMainContent = mainContent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setActiveMenu(menuDashboard);
    }

    @FXML
    private void handleDashboardClick(MouseEvent event) {
        setActiveMenu(menuDashboard);
        try {
            loadContent("/fxml/dashboard/dashboard_content.fxml");
        } catch (Exception e) {
            new Alert(Alert.AlertType.INFORMATION, "Fitur Dashboard belum tersedia.").showAndWait();
        }
    }

    @FXML
    private void handleCalendarClick(MouseEvent event) {
        setActiveMenu(menuCalendar);
        try {
            loadContent("/fxml/kalender/kalenderobat.fxml");
        } catch (Exception e) {
            new Alert(Alert.AlertType.INFORMATION, "Fitur Kalender belum tersedia.").showAndWait();
        }
    }

    @FXML
    private void handleProfileClick(MouseEvent event) {
        setActiveMenu(menuProfile);
        try {
            loadContent("/fxml/profil/profil_content.fxml");
        } catch (Exception e) {
            new Alert(Alert.AlertType.INFORMATION, "Fitur Profil belum tersedia.").showAndWait();
        }
    }

    // Hapus method handleVisualisasiClick karena menu visualisasi sudah dihapus dari sidebar

    @FXML
    private void handleLogoutClick(MouseEvent event) {
        setActiveMenu(menuLogout);
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin logout?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                utils.Session.clear();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login/login.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) staticMainContent.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("MediMate - Login");
                WindowUtil.setWindowSize(stage);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Memuat konten fxml baru ke dalam mainContent.
     */
    private void loadContent(String fxmlPath) throws Exception {
        System.out.println("[DEBUG] loadContent: " + fxmlPath);
        if (staticMainContent != null) {
            try {
                if ("/fxml/dashboard/dashboard_content.fxml".equals(fxmlPath)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                    AnchorPane content = loader.load();
                    System.out.println("[DEBUG] Dashboard content loaded: " + content);
                    Label welcomeLabel = (Label) content.lookup("#welcomeLabel");
                    Label titleLabel = (Label) content.lookup("#titleLabel");
                    if (Session.getCurrentUser() != null) {
                        String nama = Session.getCurrentUser().getNama();
                        if (welcomeLabel != null) welcomeLabel.setText("Hi, " + nama);
                        if (titleLabel != null) titleLabel.setText("Selamat Datang, " + nama + "!");
                    }
                    staticMainContent.getChildren().setAll(content);
                } else {
                    System.out.println("[DEBUG] Loading FXML: " + fxmlPath);
                    Object content = FXMLLoader.load(getClass().getResource(fxmlPath));
                    System.out.println("[DEBUG] Loaded content class: " + (content != null ? content.getClass() : "null"));
                    staticMainContent.getChildren().setAll((Node) content);
                }
            } catch (Exception e) {
                System.err.println("[DEBUG] Gagal memuat: " + fxmlPath);
                e.printStackTrace();
                throw e;
            }
        } else {
            System.err.println("[DEBUG] Main content belum diset. Gunakan setMainContent() dari controller utama.");
            throw new IllegalStateException("Main content belum diset.");
        }
    }
}
