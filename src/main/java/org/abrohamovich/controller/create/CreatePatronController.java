package org.abrohamovich.controller.create;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.abrohamovich.controller.MainController;
import org.abrohamovich.controller.NotifyDialogController;
import org.abrohamovich.dto.PatronDto;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.PatronAlreadyExistException;
import org.abrohamovich.service.interfaces.PatronService;

import java.time.LocalDate;

public class CreatePatronController {

    private final PatronService patronService;

    @FXML
    public TextField cardIdField;
    @FXML
    public TextField fullNameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField addressField;
    @FXML
    public DatePicker dateOfBirthPicker;
    @FXML
    public DatePicker registerDatePicker;

    public CreatePatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @FXML
    public void initialize() {
        cardIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9-]*")) {
                cardIdField.setText(oldValue);
            }
        });
        fullNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z -]*")) {
                fullNameField.setText(oldValue);
            }
        });
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9-]*")) {
                phoneField.setText(oldValue);
            }
        });
        addressField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9-]*")) {
                cardIdField.setText(oldValue);
            }
        });

        registerDatePicker.setEditable(false);
        LocalDate initialDate = LocalDate.now();
        registerDatePicker.setValue(LocalDate.now());

        registerDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(initialDate)) {
                registerDatePicker.setValue(initialDate);
            }
        });
    }


    @FXML
    public void createPatron(ActionEvent actionEvent) {
        PatronDto patronDto = PatronDto.builder()
                .fullName(fullNameField.getText().trim())
                .cardId(cardIdField.getText().trim())
                .email(emailField.getText().trim())
                .phone(phoneField.getText().trim())
                .address(addressField.getText().trim())
                .dateOfBirth(dateOfBirthPicker.getValue())
                .registerDate(registerDatePicker.getValue())
                .build();

        try {
            patronService.save(patronDto);
            clearFields();
            NotifyDialogController.showNotification(
                    cardIdField.getScene().getWindow(),
                    "Patron successfully saved!",
                    NotifyDialogController.NotificationType.SUCCESS
            );
        } catch (PatronAlreadyExistException | IllegalArgumentException | EntityException e) {
            NotifyDialogController.showNotification(
                    cardIdField.getScene().getWindow(),
                    e.getMessage(),
                    NotifyDialogController.NotificationType.ERROR
            );
        }
    }

    @FXML
    public void cancelCreation(ActionEvent actionEvent) {
        MainController.loadMainMenuScene();
    }

    private void clearFields() {
        cardIdField.clear();
        fullNameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        dateOfBirthPicker.setValue(null);
    }
}
