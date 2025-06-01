package org.abrohamovich.controller.create;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.abrohamovich.controller.NotifyDialogController;
import org.abrohamovich.dto.PublisherDto;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.PublisherAlreadyExistException;
import org.abrohamovich.service.interfaces.PublisherService;

import java.time.LocalDate;

public class CreatePublisherController {

    private final PublisherService publisherService;
    @FXML
    public TextField nameField;
    @FXML
    public DatePicker foundationDatePicker;
    @FXML
    public TextField addressField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField websiteField;

    public CreatePublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @FXML
    public void initialize() {
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[-`'\\p{L}\\p{M} ]*")) {
                nameField.setText(oldValue);
            }
        });

        addressField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[-`'\\d\\p{L}\\p{M} ]*")) {
                addressField.setText(oldValue);
            }
        });

        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$")) {
                emailField.setText(oldValue);
            }
        });
    }

    @FXML
    public void createPublisher(ActionEvent actionEvent) {
        String name = nameField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String website = websiteField.getText();
        LocalDate foundationDate = foundationDatePicker.getValue();

        if (name.isBlank() || address.isBlank() || email.isBlank() || website.isBlank()) {
            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Incorrect input data",
                    NotifyDialogController.NotificationType.ERROR
            );
        }

        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setName(name);
        publisherDto.setAddress(address);
        publisherDto.setEmail(email);
        publisherDto.setWebsite(website);
        publisherDto.setFoundationDate(foundationDate);

        try {
            publisherService.save(publisherDto);

            clearFields();

            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Publisher successfully saved",
                    NotifyDialogController.NotificationType.SUCCESS
            );
        } catch (PublisherAlreadyExistException e) {
            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Publisher with this name already exists",
                    NotifyDialogController.NotificationType.ERROR
            );
        } catch (EntityException e) {
            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Something went wrong while saving Author",
                    NotifyDialogController.NotificationType.ERROR
            );
        }
    }

    private void clearFields() {
        nameField.clear();
        addressField.clear();
        emailField.clear();
        websiteField.clear();
        foundationDatePicker.setValue(null);
    }

    @FXML
    public void cancelCreation(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
