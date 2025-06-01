package org.abrohamovich.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class NotifyDialogController {

    @FXML
    private Label messageLabel;
    @FXML
    private Button okButton;

    @Setter
    private Stage dialogStage;

    public static void showNotification(Window owner, String message, NotificationType type) {
        try {
            FXMLLoader loader = new FXMLLoader(NotifyDialogController.class.getResource("/notify.fxml"));
            Parent root = loader.load();

            NotifyDialogController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            if (owner != null) {
                dialogStage.initOwner(owner);
            }

            controller.setDialogStage(dialogStage);
            controller.showMessage(message, type);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setResizable(true);
            dialogStage.showAndWait();
        } catch (IOException _) {

        }
    }

    public void showMessage(String message, NotificationType type) {
        messageLabel.setText(message);
        String title;

        if (type == NotificationType.SUCCESS) {
            title = "Success!";
        } else {
            title = "Error!";
        }

        if (dialogStage != null) {
            dialogStage.setTitle(title);
        }
    }

    @FXML
    private void handleOkButton(ActionEvent event) {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    public enum NotificationType {
        SUCCESS, ERROR
    }

}
