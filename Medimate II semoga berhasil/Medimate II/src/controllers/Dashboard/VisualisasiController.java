package controllers.Dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.RincianObat;
import utils.RincianObatHelper;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import models.Reminder;
import utils.DatabaseHelper;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import controllers.SideNavigation.SideNavigationController;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

public class VisualisasiController implements Initializable {

    @FXML
    private ComboBox<String> comboBarFilter1;

    @FXML
    private ComboBox<String> comboBarFilter2;

    @FXML
    private ComboBox<String> comboPieFilter1;

    @FXML
    private ComboBox<String> comboPieFilter2;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label lihatSemuaLabel;

    @FXML
    private Label donutCenterLabel;

    @FXML
    private HBox barLegendBox;
    @FXML
    private VBox pieLegendBox;

    private final String[] legendColors = {"#fbbf24", "#2563eb", "#60a5fa", "#f87171"};
    private final String[] pieColors = {"#2563eb", "#fbbf24", "#60a5fa", "#f87171"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        addComboBoxListeners();
        populateBarChart();
        populatePieChart();
        if (lihatSemuaLabel != null) {
            lihatSemuaLabel.setOnMouseClicked(this::handleLihatSemua);
        }
    }

    private void setupComboBoxes() {
        // BarChart filter
        Set<String> namaObatSet = new HashSet<>();
        for (RincianObat r : RincianObatHelper.loadAll()) namaObatSet.add(r.getNamaObat());
        ObservableList<String> namaObatList = FXCollections.observableArrayList(namaObatSet);
        FXCollections.sort(namaObatList);
        namaObatList.add(0, "Semua");
        System.out.println("[DEBUG] setupComboBoxes - namaObatList: " + namaObatList);
        comboBarFilter1.setItems(namaObatList);
        comboBarFilter2.setItems(FXCollections.observableArrayList("Hari Ini", "Minggu Ini", "Bulan Ini"));
        comboBarFilter1.getSelectionModel().selectFirst();
        comboBarFilter2.getSelectionModel().selectFirst();
        // PieChart filter
        Set<String> kategoriSet = new HashSet<>();
        for (RincianObat r : RincianObatHelper.loadAll()) kategoriSet.add(r.getKategori());
        ObservableList<String> kategoriList = FXCollections.observableArrayList(kategoriSet);
        FXCollections.sort(kategoriList);
        kategoriList.add(0, "Semua");
        System.out.println("[DEBUG] setupComboBoxes - kategoriList: " + kategoriList);
        comboPieFilter1.setItems(kategoriList);
        comboPieFilter2.setItems(FXCollections.observableArrayList("Hari Ini", "Minggu Ini", "Bulan Ini"));
        comboPieFilter1.getSelectionModel().selectFirst();
        comboPieFilter2.getSelectionModel().selectFirst();
    }

    private void addComboBoxListeners() {
        comboBarFilter1.setOnAction(e -> populateBarChart());
        comboBarFilter2.setOnAction(e -> populateBarChart());
        comboPieFilter1.setOnAction(e -> populatePieChart());
        comboPieFilter2.setOnAction(e -> populatePieChart());
    }

    private List<RincianObat> filterByWaktu(List<RincianObat> list, String waktu) {
        LocalDate today = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int thisWeek = today.get(weekFields.weekOfWeekBasedYear());
        int thisMonth = today.getMonthValue();
        int thisYear = today.getYear();
        List<RincianObat> filtered = new java.util.ArrayList<>();
        for (RincianObat r : list) {
            LocalDate mulai = r.getTanggalMulai();
            LocalDate selesai = r.getTanggalSelesai();
            boolean masuk = false;
            if ("Hari Ini".equals(waktu)) {
                masuk = !today.isBefore(mulai) && !today.isAfter(selesai);
            } else if ("Minggu Ini".equals(waktu)) {
                for (LocalDate d = mulai; !d.isAfter(selesai); d = d.plusDays(1)) {
                    if (d.get(weekFields.weekOfWeekBasedYear()) == thisWeek && d.getYear() == thisYear) {
                        masuk = true; break;
                    }
                }
            } else if ("Bulan Ini".equals(waktu)) {
                for (LocalDate d = mulai; !d.isAfter(selesai); d = d.plusDays(1)) {
                    if (d.getMonthValue() == thisMonth && d.getYear() == thisYear) {
                        masuk = true; break;
                    }
                }
            }
            if (masuk) {
                filtered.add(r);
                System.out.println("[DEBUG] filterByWaktu - masuk: " + r.getNamaObat() + ", mulai=" + mulai + ", selesai=" + selesai);
            }
        }
        System.out.println("[DEBUG] filterByWaktu(" + waktu + "): " + filtered.size() + " data");
        if (filtered.isEmpty()) System.out.println("[DEBUG] filterByWaktu - hasil kosong!");
        return filtered;
    }

    private void populateBarChart() {
        barChart.setTitle("");
        barChart.getData().clear();
        barLegendBox.getChildren().clear();
        String filterNama = comboBarFilter1.getValue();
        String filterWaktu = comboBarFilter2.getValue();
        List<RincianObat> all = RincianObatHelper.loadAll();
        List<RincianObat> filtered = filterByWaktu(all, filterWaktu);
        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (RincianObat r : filtered) {
            if ("Semua".equals(filterNama) || r.getNamaObat().equals(filterNama)) {
                countMap.put(r.getNamaObat(), countMap.getOrDefault(r.getNamaObat(), 0) + 1);
            }
        }
        int colorIdx = 0;
        for (String nama : countMap.keySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(nama);
            series.getData().add(new XYChart.Data<>(nama, countMap.get(nama)));
            barChart.getData().add(series);
            // Manual legend
            HBox legendItem = new HBox(6);
            legendItem.setAlignment(Pos.CENTER_LEFT);
            Circle dot = new Circle(7, Color.web(legendColors[colorIdx % legendColors.length]));
            dot.getStyleClass().add("legend-dot");
            Label label = new Label(nama);
            label.getStyleClass().add("legend-label");
            legendItem.getChildren().addAll(dot, label);
            barLegendBox.getChildren().add(legendItem);
            colorIdx++;
        }
    }

    private void populatePieChart() {
        pieChart.setTitle("");
        pieChart.getData().clear();
        pieLegendBox.getChildren().clear();
        String filterKategori = comboPieFilter1.getValue();
        String filterWaktu = comboPieFilter2.getValue();
        List<RincianObat> all = RincianObatHelper.loadAll();
        List<RincianObat> filtered = filterByWaktu(all, filterWaktu);
        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (RincianObat r : filtered) {
            if ("Semua".equals(filterKategori) || r.getKategori().equals(filterKategori)) {
                countMap.put(r.getKategori(), countMap.getOrDefault(r.getKategori(), 0) + 1);
            }
        }
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        int total = 0;
        int colorIdx = 0;
        for (String kategori : countMap.keySet()) {
            int value = countMap.get(kategori);
            pieData.add(new PieChart.Data(kategori, value));
            // Manual legend
            HBox legendItem = new HBox(6);
            legendItem.setAlignment(Pos.CENTER_LEFT);
            Circle dot = new Circle(7, Color.web(pieColors[colorIdx % pieColors.length]));
            dot.getStyleClass().add("legend-dot");
            Label label = new Label(kategori);
            label.getStyleClass().add("legend-label");
            Label valueLabel = new Label(String.valueOf(value));
            valueLabel.getStyleClass().add("legend-value");
            legendItem.getChildren().addAll(dot, label, valueLabel);
            pieLegendBox.getChildren().add(legendItem);
            total += value;
            colorIdx++;
        }
        pieChart.setData(pieData);
        if (donutCenterLabel != null) {
            donutCenterLabel.setText(String.valueOf(total));
        }
        pieChart.setClockwise(true);
        pieChart.setLabelsVisible(false);
        pieChart.setStartAngle(90);
    }

    private void handleLihatSemua(MouseEvent event) {
        System.out.println("Lihat Semua diklik!");
        System.out.println("staticMainContent: " + SideNavigationController.staticMainContent);
        try {
            java.net.URL fxmlUrl = getClass().getResource("/fxml/detailrekomendasiapotek/detailrekomendasiapotek.fxml");
            System.out.println("detailrekomendasiapotek.fxml URL: " + fxmlUrl);
            AnchorPane detail = FXMLLoader.load(fxmlUrl);
            if (SideNavigationController.staticMainContent != null) {
                SideNavigationController.staticMainContent.getChildren().setAll(detail);
            } else {
                System.out.println("staticMainContent is null!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}