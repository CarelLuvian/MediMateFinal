package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class Navigation {
    public static Node load(String fxmlPath) {
        try {
            return FXMLLoader.load(Navigation.class.getResource(fxmlPath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}