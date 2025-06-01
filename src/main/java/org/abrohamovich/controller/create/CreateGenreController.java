package org.abrohamovich.controller.create;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.abrohamovich.controller.NotifyDialogController;
import org.abrohamovich.dto.GenreDto;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.GenreAlreadyExistException;
import org.abrohamovich.service.interfaces.GenreService;

public class CreateGenreController {

    private final GenreService genreService;
    @FXML
    public TextField nameField;
    @FXML
    public TextArea descriptionArea;

    public CreateGenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @FXML
    public void initialize() {
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[-`'\\p{L}\\p{M} ]*")) {
                nameField.setText(oldValue);
            }
        });
    }

    @FXML
    public void createGenre(ActionEvent actionEvent) {
        String name = nameField.getText();
        String description = descriptionArea.getText();

        if (name.isBlank() || description.isBlank()) {
            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Incorrect input data",
                    NotifyDialogController.NotificationType.ERROR
            );
        }

        GenreDto genreDto = new GenreDto();
        genreDto.setName(name);
        genreDto.setDescription(description);

        try {
            genreService.save(genreDto);

            clearFields();

            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Genre successfully saved!",
                    NotifyDialogController.NotificationType.SUCCESS
            );
        } catch (GenreAlreadyExistException e) {
            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Genre with this name already exists!",
                    NotifyDialogController.NotificationType.ERROR
            );
        } catch (EntityException e) {
            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Something went wrong while saving genre!",
                    NotifyDialogController.NotificationType.ERROR
            );
        }
    }

    private void clearFields() {
        nameField.clear();
        descriptionArea.clear();
    }

    @FXML
    public void cancelCreation(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
