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
import org.abrohamovich.dto.GenreDto;
import org.abrohamovich.exceptions.GenreNotFoundException;
import org.abrohamovich.service.interfaces.GenreService;

import java.util.List;

public class ManageGenreController {

    private final GenreService genreService;
    @FXML
    public VBox editPanel;
    @FXML
    public TextField filterTextField;
    @FXML
    public ListView<GenreDto> genreListView;
    @FXML
    public TextField nameField;
    @FXML
    public TextField descriptionField;
    private ObservableList<GenreDto> genres;
    private GenreDto selectedGenreForEdit;

    public ManageGenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @FXML
    public void initialize() {
        genres = FXCollections.observableArrayList();
        genreListView.setItems(genres);
        genreListView.setCellFactory(lv -> new GenreCell());
        loadAllGenres();

        nameField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[-`'\\p{L}\\p{M} ]*")) {
                nameField.setText(oldValue);
            }
        });

        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    @FXML
    public void filterGenres(KeyEvent keyEvent) {
        String filterName = filterTextField.getText().trim();
        if (filterName.isEmpty()) {
            loadAllGenres();
        } else {
            List<GenreDto> foundGenres = genreService.findByNameLike(filterName);
            genres.clear();
            genres.addAll(foundGenres);
        }
    }

    private void loadAllGenres() {
        List<GenreDto> allGenres = genreService.findAll();
        genres.setAll(allGenres);
    }

    @FXML
    public void returnToMainMenu(MouseEvent mouseEvent) {
        MainController.loadMainMenuScene();
    }

    @FXML
    public void saveGenre(ActionEvent actionEvent) {
        if (selectedGenreForEdit == null) return;

        String newName = nameField.getText();
        String newDescription = descriptionField.getText();

        if (newName.isBlank() || newDescription.isBlank()) {
            NotifyDialogController.showNotification(
                    ((Node) actionEvent.getSource()).getScene().getWindow(),
                    "Incorrect input data",
                    NotifyDialogController.NotificationType.ERROR
            );
        }

        selectedGenreForEdit.setName(newName);
        selectedGenreForEdit.setDescription(newDescription);

        try {
            genreService.update(selectedGenreForEdit);

            NotifyDialogController.showNotification(
                    ((Node) actionEvent.getSource()).getScene().getWindow(),
                    "Genre was updated successfully!",
                    NotifyDialogController.NotificationType.SUCCESS
            );
        } catch (GenreNotFoundException e) {
            NotifyDialogController.showNotification(
                    ((Node) actionEvent.getSource()).getScene().getWindow(),
                    "Genre you want to update does not exist!",
                    NotifyDialogController.NotificationType.ERROR
            );
        }

        loadAllGenres();

        cancelEdit(null);
    }

    @FXML
    public void cancelEdit(ActionEvent actionEvent) {
        selectedGenreForEdit = null;
        nameField.clear();
        descriptionField.clear();
        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    private class GenreCell extends ListCell<GenreDto> {
        private final HBox hbox = new HBox();
        private final Label nameLabel = new Label();
        private final Button editButton = new Button("Edit");
        private final Pane spacer = new Pane();

        public GenreCell() {
            super();
            hbox.setSpacing(10);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hbox.getChildren().addAll(nameLabel, spacer, editButton);

            editButton.setOnAction(event -> {
                GenreDto genre = getItem();
                if (genre != null) {
                    selectedGenreForEdit = genre;
                    nameField.setText(genre.getName());
                    descriptionField.setText(genre.getDescription());
                    editPanel.setVisible(true);
                    editPanel.setManaged(true);
                }
            });
        }

        @Override
        protected void updateItem(GenreDto item, boolean empty) {
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
