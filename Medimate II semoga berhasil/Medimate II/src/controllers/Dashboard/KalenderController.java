package controllers.Dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import models.RincianObat;
import utils.RincianObatHelper;
import java.util.ArrayList;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import controllers.SideNavigation.SideNavigationController;
import models.Reminder;
import utils.DatabaseHelper;
import javafx.scene.image.ImageView;

public class KalenderController {

    @FXML
    private Label labelBulanTahun;

    @FXML
    private GridPane gridTanggal;

    @FXML
    private Button btnPrev, btnNext;

    @FXML private VBox jadwalObatContainer;
    @FXML private Label labelJadwalObat;

    private int bulan = 6; // 6 = Juni (0-based)
    private int tahun = 2025;

    // Simpan referensi tanggal agar bisa diganti warnanya saat diklik
    private HashMap<Label, Integer> tanggalMap = new HashMap<>();

    private int selectedTanggal = -1;

    @FXML
    public void initialize() {
        System.out.println("[DEBUG] KalenderController initialize");
        updateKalender();
        // Auto-pilih tanggal hari ini jika bulan/tahun sama
        LocalDate today = LocalDate.now();
        if (today.getMonthValue() == (bulan + 1) && today.getYear() == tahun) {
            selectedTanggal = today.getDayOfMonth();
            updateJadwalObatList();
        }
    }

    @FXML
    private void handlePrevBulan() {
        bulan--;
        if (bulan < 0) {
            bulan = 11;
            tahun--;
        }
        System.out.println("[DEBUG] handlePrevBulan: " + bulan + ", " + tahun);
        updateKalender();
    }

    @FXML
    private void handleNextBulan() {
        bulan++;
        if (bulan > 11) {
            bulan = 0;
            tahun++;
        }
        System.out.println("[DEBUG] handleNextBulan: " + bulan + ", " + tahun);
        updateKalender();
    }

    @FXML
    private void handleLihatSemua() {
        // Navigasi ke halaman kalender utama
        if (SideNavigationController.staticMainContent != null) {
            try {
                AnchorPane content = FXMLLoader.load(getClass().getResource("/fxml/kalender/kalender_content.fxml"));
                SideNavigationController.staticMainContent.getChildren().setAll(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleTanggalClick(MouseEvent event) {
        Label clickedLabel = (Label) event.getSource();
        int tanggalDipilih = Integer.parseInt(clickedLabel.getText());
        selectedTanggal = tanggalDipilih;
        System.out.println("[DEBUG] handleTanggalClick: tanggalDipilih=" + tanggalDipilih);
        // Reset semua label
        for (Label l : tanggalMap.keySet()) {
            l.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(15), Insets.EMPTY)));
        }
        // Tandai tanggal yang diklik
        clickedLabel.setBackground(new Background(new BackgroundFill(Color.web("#CFE3FF"), new CornerRadii(15), Insets.EMPTY)));
        updateJadwalObatList();
    }

    private void updateKalender() {
        System.out.println("[DEBUG] updateKalender: bulan=" + bulan + ", tahun=" + tahun);
        gridTanggal.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

        String namaBulan = Month.of(bulan + 1).name().substring(0, 1).toUpperCase() + Month.of(bulan + 1).name().substring(1).toLowerCase();
        labelBulanTahun.setText(namaBulan + " " + tahun);

        tanggalMap.clear();

        // Ambil semua rincian obat
        List<RincianObat> rincianList = RincianObatHelper.loadAll();
        System.out.println("[DEBUG] Jumlah rincian obat: " + rincianList.size());
        // Buat map tanggal yang perlu di-highlight
        HashMap<Integer, Boolean> tanggalAdaObat = new HashMap<>();
        for (RincianObat r : rincianList) {
            LocalDate mulai = r.getTanggalMulai();
            LocalDate selesai = r.getTanggalSelesai();
            if (mulai.getMonthValue() == (bulan + 1) && mulai.getYear() == tahun) {
                for (int t = mulai.getDayOfMonth(); t <= selesai.getDayOfMonth(); t++) {
                    tanggalAdaObat.put(t, true);
                }
            } else if (selesai.getMonthValue() == (bulan + 1) && selesai.getYear() == tahun) {
                for (int t = 1; t <= selesai.getDayOfMonth(); t++) {
                    tanggalAdaObat.put(t, true);
                }
            } else if (mulai.getYear() < tahun && selesai.getYear() > tahun) {
                // seluruh bulan di tahun ini
                YearMonth ym = YearMonth.of(tahun, bulan + 1);
                for (int t = 1; t <= ym.lengthOfMonth(); t++) {
                    tanggalAdaObat.put(t, true);
                }
            } else if (mulai.getYear() == tahun && mulai.getMonthValue() < (bulan + 1) && selesai.getYear() == tahun && selesai.getMonthValue() > (bulan + 1)) {
                // seluruh bulan di tahun ini
                YearMonth ym = YearMonth.of(tahun, bulan + 1);
                for (int t = 1; t <= ym.lengthOfMonth(); t++) {
                    tanggalAdaObat.put(t, true);
                }
            }
        }

        int row = 1;
        int column = 0;
        YearMonth ym = YearMonth.of(tahun, bulan + 1);
        int daysInMonth = ym.lengthOfMonth();

        for (int i = 1; i <= daysInMonth; i++) {
            Label lbl = new Label(String.valueOf(i));
            lbl.setFont(Font.font(13));
            lbl.setPrefSize(30, 30);
            lbl.setMinSize(30, 30);
            lbl.setMaxSize(30, 30);
            lbl.setStyle("-fx-text-fill: #111827;"); // pastikan warna font hitam
            lbl.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(15), Insets.EMPTY)));

            // Highlight jika ada obat
            if (tanggalAdaObat.containsKey(i)) {
                lbl.setBackground(new Background(new BackgroundFill(Color.web("#CFE3FF"), new CornerRadii(15), Insets.EMPTY)));
            }

            final int tanggal = i;
            lbl.setOnMouseClicked((MouseEvent e) -> {
                for (Label l : tanggalMap.keySet()) {
                    l.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(15), Insets.EMPTY)));
                    l.setStyle("-fx-text-fill: #111827;"); // reset warna font hitam
                }
                lbl.setBackground(new Background(new BackgroundFill(Color.web("#CFE3FF"), new CornerRadii(15), Insets.EMPTY)));
                lbl.setStyle("-fx-text-fill: #111827;"); // tetap hitam meski di-highlight
                selectedTanggal = tanggal;
                System.out.println("[DEBUG] Tanggal diklik: " + tanggal);
                updateJadwalObatList();
            });

            GridPane.setRowIndex(lbl, row);
            GridPane.setColumnIndex(lbl, column);

            gridTanggal.getChildren().add(lbl);
            tanggalMap.put(lbl, tanggal);

            column++;
            if (column == 7) {
                column = 0;
                row++;
            }
        }
        // Reset list jadwal jika bulan/tahun berubah
        // selectedTanggal = -1; // HAPUS baris ini agar selectedTanggal tetap bisa diatur

        // Tambahkan logika highlight otomatis jika tidak ada tanggal yang dipilih
        if (selectedTanggal == -1 && !tanggalAdaObat.isEmpty()) {
            int lastTanggal = tanggalAdaObat.keySet().stream().max(Integer::compareTo).orElse(1);
            for (Label l : tanggalMap.keySet()) {
                if (tanggalMap.get(l) == lastTanggal) {
                    l.setBackground(new Background(new BackgroundFill(Color.web("#CFE3FF"), new CornerRadii(15), Insets.EMPTY)));
                    selectedTanggal = lastTanggal;
                    break;
                }
            }
        }
        updateJadwalObatList();
    }

    private void updateJadwalObatList() {
        System.out.println("[DEBUG] updateJadwalObatList: selectedTanggal=" + selectedTanggal);
        List<RincianObat> rincianList = RincianObatHelper.loadAll();
        List<RincianObat> filtered = new ArrayList<>();
        if (selectedTanggal > 0) {
            LocalDate tanggal = LocalDate.of(tahun, bulan + 1, selectedTanggal);
            for (RincianObat r : rincianList) {
                LocalDate mulai = r.getTanggalMulai();
                LocalDate selesai = r.getTanggalSelesai();
                if (!tanggal.isBefore(mulai) && !tanggal.isAfter(selesai)) {
                    filtered.add(r);
                }
            }
        }
        System.out.println("[DEBUG] Jumlah jadwal pada tanggal ini: " + filtered.size());
        if (filtered.isEmpty()) {
            // Tampilkan image dan label default
            ImageView img = new ImageView("/images/calendar-icon.png");
            img.setFitWidth(40);
            img.setFitHeight(40);
            Label lbl = new Label("Tidak ada jadwal minum obat");
            lbl.getStyleClass().add("label-subtitle");
            jadwalObatContainer.getChildren().setAll(img, lbl);
            System.out.println("[DEBUG] jadwalObatContainer: tampilkan default");
        } else {
            // Tampilkan box-box nama obat dan waktu minum
            List<Region> boxList = new ArrayList<>();
            for (RincianObat r : filtered) {
                VBox box = new VBox(2);
                box.setStyle("-fx-background-color: #E0F2FE; -fx-background-radius: 8; -fx-padding: 8 12 8 12; -fx-spacing: 2; -fx-alignment: CENTER_LEFT;");
                Label nama = new Label(r.getNamaObat());
                nama.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #0A2647;");
                Label waktu = new Label(r.getWaktuMinum());
                waktu.setStyle("-fx-font-size: 13px; -fx-text-fill: #2563eb;");
                box.getChildren().addAll(nama, waktu);
                boxList.add(box);
            }
            jadwalObatContainer.getChildren().setAll(boxList);
            System.out.println("[DEBUG] jadwalObatContainer: tampilkan " + boxList.size() + " box jadwal");
        }
    }
}
