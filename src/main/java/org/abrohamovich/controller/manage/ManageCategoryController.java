package org.abrohamovich.controller.manage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.abrohamovich.controller.MainController;
import org.abrohamovich.controller.NotifyDialogController;
import org.abrohamovich.dto.CategoryDto;
import org.abrohamovich.exceptions.CategoryNotFoundException;
import org.abrohamovich.service.interfaces.CategoryService;

import java.util.List;

public class ManageCategoryController {

    private final CategoryService categoryService;
    @FXML
    public VBox editPanel;
    @FXML
    public TextField filterTextField;
    @FXML
    public ListView<CategoryDto> categoryListView;
    @FXML
    public TextField nameField;
    @FXML
    public TextField descriptionField;
    private ObservableList<CategoryDto> categories;
    private CategoryDto selectedCategoryForEdit;

    public ManageCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @FXML
    public void returnToMainMenu(MouseEvent mouseEvent) {
        MainController.loadMainMenuScene();
    }

    @FXML
    public void initialize() {
        categories = FXCollections.observableArrayList();
        categoryListView.setItems(categories);
        categoryListView.setCellFactory(lv ->
                new ManageCategoryController.CategoryCell());
        loadAllCategories();

        nameField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[-`'\\p{L}\\p{M} ]*")) {
                nameField.setText(oldValue);
            }
        });

        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    @FXML
    public void filterCategories(KeyEvent keyEvent) {
        String filterName = filterTextField.getText().trim();
        if (filterName.isEmpty()) {
            loadAllCategories();
        } else {
            List<CategoryDto> foundCategory = categoryService.findByNameLike(filterName);
            categories.clear();
            categories.addAll(foundCategory);
        }
    }

    private void loadAllCategories() {
        List<CategoryDto> allCategories = categoryService.findAll();
        categories.setAll(allCategories);
    }

    @FXML
    public void saveCategory(ActionEvent actionEvent) {
        if (selectedCategoryForEdit == null) return;

        selectedCategoryForEdit.setName(nameField.getText().trim());
        selectedCategoryForEdit.setDescription(descriptionField.getText().trim());

        try {
            categoryService.update(selectedCategoryForEdit);
            NotifyDialogController.showNotification(
                    ((Node) actionEvent.getSource()).getScene().getWindow(),
                    "Category was updated successfully!",
                    NotifyDialogController.NotificationType.SUCCESS
            );
        } catch (CategoryNotFoundException | IllegalArgumentException e) {
            NotifyDialogController.showNotification(
                    ((Node) actionEvent.getSource()).getScene().getWindow(),
                    e.getMessage(),
                    NotifyDialogController.NotificationType.ERROR
            );
        }
        loadAllCategories();
        cancelEdit(null);
    }

    @FXML
    public void cancelEdit(ActionEvent actionEvent) {
        selectedCategoryForEdit = null;
        nameField.clear();
        descriptionField.clear();
        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    private class CategoryCell extends ListCell<CategoryDto> {
        private final HBox hbox = new HBox();
        private final Label nameLabel = new Label();
        private final Button editButton = new Button("Edit");
        private final Pane spacer = new Pane();

        public CategoryCell() {
            super();
            hbox.setSpacing(10);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hbox.getChildren().addAll(nameLabel, spacer, editButton);

            editButton.setOnAction(event -> {
                CategoryDto category = getItem();
                if (category != null) {
                    selectedCategoryForEdit = category;
                    nameField.setText(category.getName());
                    descriptionField.setText(category.getDescription());
                    editPanel.setVisible(true);
                    editPanel.setManaged(true);
                }
            });
        }

        @Override
        protected void updateItem(CategoryDto item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                nameLabel.setText(item.toString());
                setGraphic(hbox);
            }
        }
    }
}
