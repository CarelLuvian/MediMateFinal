package utils;

import javafx.stage.Stage;

public class WindowUtil {
    public static void setWindowSize(Stage stage) {
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
    }

    public static void setFixedWindowSize(Stage stage) {
        stage.setWidth(1280);
        stage.setHeight(800);
        stage.setMinWidth(1280);
        stage.setMinHeight(800);
        stage.setMaxWidth(1280);
        stage.setMaxHeight(800);
    }
} 