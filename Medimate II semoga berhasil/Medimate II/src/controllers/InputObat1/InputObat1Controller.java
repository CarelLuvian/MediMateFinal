package controllers.InputObat1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class InputObat1Controller {
    @FXML private TextField namaObatField;
    @FXML private ComboBox<String> kategoriComboBox;
    @FXML private TextField dosisField;
    @FXML private ComboBox<String> frekuensiComboBox;

    // Simpan data sementara di Session
    public static Map<String, String> tempObat1 = new HashMap<>();

    @FXML
    public void initialize() {
        if (kategoriComboBox != null) {
            kategoriComboBox.getItems().addAll("Analgesik", "Antibiotik", "Vitamin", "Antipiretik", "Antasida");
        }
        if (frekuensiComboBox != null) {
            frekuensiComboBox.getItems().addAll("1x sehari", "2x sehari", "3x sehari", "4x sehari");
        }
    }

    @FXML
    private void handleLanjutkan(ActionEvent event) {
        // Simpan data input ke static map
        tempObat1.put("namaObat", namaObatField.getText());
        tempObat1.put("kategori", kategoriComboBox.getValue());
        tempObat1.put("dosis", dosisField.getText());
        tempObat1.put("frekuensi", frekuensiComboBox.getValue());
        try {
            javafx.scene.Parent inputObat2Page = javafx.fxml.FXMLLoader.load(getClass().getResource("/fxml/inputobat2/inputobat2.fxml"));
            controllers.SideNavigation.SideNavigationController.staticMainContent.getChildren().setAll((javafx.scene.Node) inputObat2Page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 