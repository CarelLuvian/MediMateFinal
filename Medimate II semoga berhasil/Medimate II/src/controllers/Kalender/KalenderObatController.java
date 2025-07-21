package controllers.Kalender;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import models.Reminder;
import utils.DatabaseHelper;
import models.RincianObat;
import utils.RincianObatHelper;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.image.ImageView;
import java.io.File;
import javafx.scene.image.Image;
import models.User;

public class KalenderObatController implements Initializable {

    @FXML private ToggleButton mendatangTab;
    @FXML private ToggleButton yangLaluTab;
    @FXML private ComboBox<String> monthComboBox;
    @FXML private Button addScheduleButton;
    @FXML private VBox scheduleContainer;
    @FXML private ImageView avatarImageView;

    private ToggleGroup tabGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set avatar di pojok kanan atas
        User user = utils.Session.getCurrentUser();
        if (avatarImageView != null && user != null) {
            String ext = ".png";
            String avatarPath = "src/images/avatar_" + user.getEmail().replaceAll("[^a-zA-Z0-9]", "") + ext;
            File avatarFile = new File(avatarPath);
            File defaultAvatar = new File("src/images/avatar.png");
            avatarImageView.setImage(
                avatarFile.exists()
                    ? new Image(avatarFile.toURI().toString())
                    : (defaultAvatar.exists() ? new Image(defaultAvatar.toURI().toString()) : null)
            );
        }
        tabGroup = new ToggleGroup();
        mendatangTab.setToggleGroup(tabGroup);
        yangLaluTab.setToggleGroup(tabGroup);

        tabGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                updateScheduleList();
            }
        });

        monthComboBox.getItems().addAll("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember");
        monthComboBox.getSelectionModel().selectFirst();
        monthComboBox.setOnAction(e -> updateScheduleList());

        addScheduleButton.setOnAction(e -> handleAddSchedule());

        updateScheduleList();
    }

    private void updateScheduleList() {
        scheduleContainer.getChildren().clear();
        List<RincianObat> rincianList = RincianObatHelper.loadAll();
        java.time.LocalDate today = java.time.LocalDate.now();
        
        // Get selected month index (0-based)
        int selectedMonthIndex = monthComboBox.getSelectionModel().getSelectedIndex();
        // Add 1 to convert to 1-based month number
        int selectedMonth = selectedMonthIndex + 1;
        
        String status = getSelectedStatus();
        for (RincianObat r : rincianList) {
            java.time.LocalDate tanggalMulai = r.getTanggalMulai();
            java.time.LocalDate tanggalSelesai = r.getTanggalSelesai();
            
            // Check if the schedule falls within the selected month
            boolean isInSelectedMonth = (tanggalMulai.getMonthValue() == selectedMonth) || 
                                      (tanggalSelesai.getMonthValue() == selectedMonth) ||
                                      (tanggalMulai.getMonthValue() < selectedMonth && 
                                       tanggalSelesai.getMonthValue() > selectedMonth);
            
            if (!isInSelectedMonth) {
                continue; // Skip if not in selected month
            }

            if ("mendatang".equals(status) && (tanggalMulai.isEqual(today) || tanggalMulai.isAfter(today))) {
                scheduleContainer.getChildren().add(createRincianObatItem(r));
            } else if ("selesai".equals(status) && tanggalMulai.isBefore(today)) {
                scheduleContainer.getChildren().add(createRincianObatItem(r));
            }
        }
        
        // If no items found, show a message
        if (scheduleContainer.getChildren().isEmpty()) {
            Label emptyLabel = new Label("Tidak ada jadwal minum obat untuk bulan " + monthComboBox.getValue());
            emptyLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
            scheduleContainer.getChildren().add(emptyLabel);
        }
    }

    private HBox createRincianObatItem(RincianObat r) {
        HBox item = new HBox(10);
        item.setPadding(new Insets(10));
        item.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.web("#F3F4F6"), new CornerRadii(6), Insets.EMPTY)));
        Label nama = new Label(r.getNamaObat());
        nama.setStyle("-fx-font-size: 16px; -fx-text-fill: #111827; -fx-font-weight: bold;");
        Label waktu = new Label(r.getWaktuMinum() + " | " + r.getTanggalMulai().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        waktu.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button hapusButton = new Button("Hapus");
        hapusButton.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-background-radius: 6;");
        hapusButton.setOnAction(event -> {
            List<RincianObat> list = RincianObatHelper.loadAll();
            list.removeIf(x -> x.getNamaObat().equals(r.getNamaObat()) && x.getWaktuMinum().equals(r.getWaktuMinum()));
            RincianObatHelper.saveAll(list);
            updateScheduleList();
        });
        item.getChildren().addAll(nama, waktu, spacer, hapusButton);
        return item;
    }

    private String getSelectedStatus() {
        if (mendatangTab.isSelected()) return "mendatang";
        if (yangLaluTab.isSelected()) return "selesai";
        return "mendatang";
    }

    @FXML
    private void handleAddSchedule() {
        // Navigasi ke halaman inputobat1.fxml di main content
        try {
            javafx.scene.Parent inputObatPage = javafx.fxml.FXMLLoader.load(getClass().getResource("/fxml/inputobat1/inputobat1.fxml"));
            controllers.SideNavigation.SideNavigationController.staticMainContent.getChildren().setAll(inputObatPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
