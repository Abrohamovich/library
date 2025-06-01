package org.abrohamovich.controller.create;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.abrohamovich.controller.NotifyDialogController;
import org.abrohamovich.dto.CategoryDto;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.GenreAlreadyExistException;
import org.abrohamovich.service.interfaces.CategoryService;

public class CreateCategoryController {

    private final CategoryService categoryService;
    @FXML
    public TextField nameField;
    @FXML
    public TextArea descriptionArea;

    public CreateCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
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
    public void createCategory(ActionEvent actionEvent) {
        String name = nameField.getText();
        String description = descriptionArea.getText();

        if (name.isBlank() || description.isBlank()) {
            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Incorrect input data!",
                    NotifyDialogController.NotificationType.ERROR
            );
        }

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);
        categoryDto.setDescription(description);

        try {
            categoryService.save(categoryDto);

            clearFields();

            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Category successfully saved!",
                    NotifyDialogController.NotificationType.SUCCESS
            );
        } catch (GenreAlreadyExistException e) {
            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Category with this name already exists!",
                    NotifyDialogController.NotificationType.ERROR
            );
        } catch (EntityException e) {
            NotifyDialogController.showNotification(
                    nameField.getScene().getWindow(),
                    "Something went wrong while saving category!",
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
