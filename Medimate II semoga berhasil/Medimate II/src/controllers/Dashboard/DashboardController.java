package controllers.Dashboard;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import controllers.SideNavigation.SideNavigationController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.Session;
import utils.WindowUtil;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.io.File;

public class DashboardController {

    @FXML
    private AnchorPane mainContentPane;

    @FXML
    public void initialize() {
        SideNavigationController.staticMainContent = mainContentPane;
        javafx.application.Platform.runLater(() -> {
            if (Session.getCurrentUser() == null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login/login.fxml"));
                    Scene scene = new Scene(loader.load());
                    Stage stage = (Stage) mainContentPane.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("MediMate - Login");
                    WindowUtil.setWindowSize(stage);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard/dashboard_content.fxml"));
                    AnchorPane dashboardContent = loader.load();
                    // Set welcomeLabel dan userNameLabel sesuai user
                    Label welcomeLabel = (Label) dashboardContent.lookup("#welcomeLabel");
                    System.out.println("[DEBUG] welcomeLabel: " + welcomeLabel);
                    Label userNameLabel = (Label) dashboardContent.lookup("#userNameLabel");
                    System.out.println("[DEBUG] userNameLabel: " + userNameLabel);
                    ImageView avatarImageView = (ImageView) dashboardContent.lookup("#avatarImageView");
                    if (avatarImageView != null && utils.Session.getCurrentUser() != null) {
                        String ext = ".png";
                        String avatarPath = "src/images/avatar_" + utils.Session.getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]", "") + ext;
                        File avatarFile = new File(avatarPath);
                        File defaultAvatar = new File("src/images/avatar.png");
                        avatarImageView.setImage(
                            avatarFile.exists()
                                ? new javafx.scene.image.Image(avatarFile.toURI().toString())
                                : (defaultAvatar.exists() ? new javafx.scene.image.Image(defaultAvatar.toURI().toString()) : null)
                        );
                    }
                    String nama = utils.Session.getCurrentUser().getNama();
                    if (welcomeLabel != null) welcomeLabel.setText("Hi, " + nama);
                    if (userNameLabel != null) userNameLabel.setText(nama);
                    mainContentPane.getChildren().setAll(dashboardContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}