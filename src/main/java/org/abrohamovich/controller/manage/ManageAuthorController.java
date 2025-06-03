package org.abrohamovich.controller.manage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.abrohamovich.controller.MainController;
import org.abrohamovich.controller.NotifyDialogController;
import org.abrohamovich.dto.AuthorDto;
import org.abrohamovich.entity.Sex;
import org.abrohamovich.exceptions.AuthorNotFoundException;
import org.abrohamovich.service.interfaces.AuthorService;

import java.util.List;

public class ManageAuthorController {

    private final AuthorService authorService;
    @FXML
    public TextField filterTextField;
    @FXML
    public ListView<AuthorDto> authorListView;
    @FXML
    public VBox editPanel;
    @FXML
    public TextField fullNameField;
    @FXML
    public DatePicker dateOfBirthPicker;
    @FXML
    public ComboBox<Sex> sexComboBox;
    @FXML
    public TextField nationalityField;
    private ObservableList<AuthorDto> authors;
    private AuthorDto selectedAuthorForEdit;

    public ManageAuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @FXML
    public void initialize() {
        authors = FXCollections.observableArrayList();
        authorListView.setItems(authors);
        authorListView.setCellFactory(lv ->
                new ManageAuthorController.AuthorCell());
        loadAllAuthors();

        fullNameField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[-`'\\p{L}\\p{M} ]*")) {
                fullNameField.setText(oldValue);
            }
        });

        nationalityField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[-'\\p{L}\\p{M} ]*")) {
                nationalityField.setText(oldValue);
            }
        });

        sexComboBox.setItems(FXCollections.observableArrayList(Sex.values()));

        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    @FXML
    public void filterAuthors(KeyEvent keyEvent) {
        String filterFullName = filterTextField.getText();
        if (filterFullName.isBlank()) {
            loadAllAuthors();
        } else {
            List<AuthorDto> foundAuthor = authorService.findByFullNameLike(filterFullName);
            authors.clear();
            authors.addAll(foundAuthor);
        }
    }

    private void loadAllAuthors() {
        authors.clear();
        List<AuthorDto> allAuthors = authorService.findAll();
        authors.addAll(allAuthors);
    }

    @FXML
    public void returnToMainMenu(MouseEvent mouseEvent) {
        MainController.loadMainMenuScene();
    }

    @FXML
    public void saveAuthor(ActionEvent actionEvent) {
        if (selectedAuthorForEdit == null) return;

        selectedAuthorForEdit.setFullName(fullNameField.getText().trim());
        selectedAuthorForEdit.setNationality(nationalityField.getText().trim());
        selectedAuthorForEdit.setSex(sexComboBox.getValue());
        selectedAuthorForEdit.setDateOfBirth(dateOfBirthPicker.getValue());

        try {
            authorService.update(selectedAuthorForEdit);
            NotifyDialogController.showNotification(
                    fullNameField.getScene().getWindow(),
                    "Author successfully updated",
                    NotifyDialogController.NotificationType.SUCCESS
            );
        } catch (AuthorNotFoundException | IllegalArgumentException e) {
            NotifyDialogController.showNotification(
                    fullNameField.getScene().getWindow(),
                    e.getMessage(),
                    NotifyDialogController.NotificationType.ERROR
            );
        }
        loadAllAuthors();
        cancelEdit(null);
    }

    @FXML
    public void cancelEdit(ActionEvent actionEvent) {
        selectedAuthorForEdit = null;
        fullNameField.clear();
        nationalityField.clear();
        dateOfBirthPicker.setValue(null);
        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    public class AuthorCell extends ListCell<AuthorDto> {
        private final HBox hbox = new HBox();
        private final Label nameLabel = new Label();
        private final Button editButton = new Button("Edit");
        private final Pane spacer = new Pane();

        public AuthorCell() {
            super();
            hbox.setSpacing(10);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hbox.getChildren().addAll(nameLabel, spacer, editButton);

            editButton.setOnAction(event -> {
                AuthorDto authorDto = getItem();
                if (authorDto != null) {
                    selectedAuthorForEdit = authorDto;
                    fullNameField.setText(authorDto.getFullName());
                    dateOfBirthPicker.setValue(authorDto.getDateOfBirth());
                    sexComboBox.setValue(authorDto.getSex());
                    nationalityField.setText(authorDto.getNationality());
                    editPanel.setVisible(true);
                    editPanel.setManaged(true);
                }
            });
        }

        @Override
        protected void updateItem(AuthorDto authorDto, boolean b) {
            super.updateItem(authorDto, b);
            setText(null);
            if (b || authorDto == null) {
                setGraphic(null);
            } else {
                nameLabel.setText(authorDto.toString());
                setGraphic(hbox);
            }
        }
    }
}
