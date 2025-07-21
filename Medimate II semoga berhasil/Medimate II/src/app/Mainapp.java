package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import utils.WindowUtil;
import utils.DatabaseHelper;

public class Mainapp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseHelper.loadUsersFromXML();
            System.out.println("Memuat dashboard.fxml...");
            URL fxmlLocation = getClass().getResource("/fxml/dashboard/dashboard.fxml");
            System.out.println("fxmlLocation: " + fxmlLocation);

            if (fxmlLocation == null) {
                throw new RuntimeException("File FXML tidak ditemukan di path: /fxml/dashboard/dashboard.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Scene scene = new Scene(root, 1280, 800); // Set fixed size here
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            primaryStage.setTitle("MediMate");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Disable resizing
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
