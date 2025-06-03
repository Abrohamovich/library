package org.abrohamovich.controller.create;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.abrohamovich.controller.NotifyDialogController;
import org.abrohamovich.dto.AuthorDto;
import org.abrohamovich.entity.Sex;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.service.interfaces.AuthorService;

import java.util.List;

public class CreateAuthorController {

    private final AuthorService authorService;
    @FXML
    public TextField fullNameField;
    @FXML
    public DatePicker dateOfBirthPicker;
    @FXML
    public ComboBox<Sex> sexComboBox;
    @FXML
    public TextField nationalityField;

    public CreateAuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @FXML
    public void initialize() {
        fullNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[-`'\\p{L}\\p{M} ]*")) {
                fullNameField.setText(oldValue);
            }
        });

        nationalityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[-'\\p{L}\\p{M} ]*")) {
                nationalityField.setText(oldValue);
            }
        });

        sexComboBox.setItems(FXCollections.observableList(List.of(Sex.MALE, Sex.FEMALE)));
        sexComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void createAuthor(ActionEvent actionEvent) {
        AuthorDto authorDto = AuthorDto.builder()
                .fullName(fullNameField.getText().trim())
                .dateOfBirth(dateOfBirthPicker.getValue())
                .sex(sexComboBox.getValue())
                .nationality(nationalityField.getText().trim())
                .build();

        try {
            authorService.save(authorDto);
            clearFields();
            NotifyDialogController.showNotification(
                    fullNameField.getScene().getWindow(),
                    "Author successfully saved!",
                    NotifyDialogController.NotificationType.SUCCESS
            );
        } catch (IllegalArgumentException | EntityException e) {
            NotifyDialogController.showNotification(
                    fullNameField.getScene().getWindow(),
                    e.getMessage(),
                    NotifyDialogController.NotificationType.ERROR
            );
        }
    }

    @FXML
    public void cancelCreation(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    private void clearFields() {
        fullNameField.clear();
        nationalityField.clear();
        dateOfBirthPicker.setValue(null);
    }
}
