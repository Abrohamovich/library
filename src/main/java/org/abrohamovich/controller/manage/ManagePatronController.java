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
import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.controller.MainController;
import org.abrohamovich.controller.NotifyDialogController;
import org.abrohamovich.dto.PatronDto;
import org.abrohamovich.exceptions.PatronNotFoundException;
import org.abrohamovich.service.interfaces.PatronService;

import java.util.List;

@Slf4j
public class ManagePatronController {

    private final PatronService patronService;
    @FXML
    public TextField filterTextField;
    @FXML
    public ListView<PatronDto> patronListView;
    @FXML
    public VBox editPanel;
    @FXML
    public TextField fullNameField;
    @FXML
    public TextField cardIdField;
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
    private ObservableList<PatronDto> patrons;
    private PatronDto selectedPatronForEdit;

    public ManagePatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @FXML
    public void initialize() {
        patrons = FXCollections.observableArrayList();
        patronListView.setItems(patrons);
        patronListView.setCellFactory(lv ->
                new ManagePatronController.PatronCell());
        loadAllPatrons();

        cardIdField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9-]*")) {
                cardIdField.setText(oldValue);
            }
        });
        fullNameField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z -]*")) {
                fullNameField.setText(oldValue);
            }
        });
        phoneField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[0-9-]*")) {
                phoneField.setText(oldValue);
            }
        });
        addressField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9-]*")) {
                cardIdField.setText(oldValue);
            }
        });

        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    @FXML
    public void filterPatrons(KeyEvent keyEvent) {
        String filterText = filterTextField.getText();
        if (filterText.isEmpty()) {
            loadAllPatrons();
        } else {
            List<PatronDto> foundPatrons = patronService.findByFullName(filterText);
            patrons.clear();
            patrons.addAll(foundPatrons);
        }
    }

    private void loadAllPatrons() {
        patrons.clear();
        List<PatronDto> allPatrons = patronService.findAll();
        patrons.addAll(allPatrons);
    }

    @FXML
    public void returnToMainMenu(MouseEvent mouseEvent) {
        MainController.loadMainMenuScene();
    }

    @FXML
    public void savePatron(ActionEvent actionEvent) {
        if (selectedPatronForEdit == null) return;

        selectedPatronForEdit.setFullName(fullNameField.getText().trim());
        selectedPatronForEdit.setEmail(emailField.getText().trim());
        selectedPatronForEdit.setPhone(phoneField.getText().trim());
        selectedPatronForEdit.setAddress(addressField.getText().trim());
        selectedPatronForEdit.setDateOfBirth(dateOfBirthPicker.getValue());
        selectedPatronForEdit.setRegisterDate(registerDatePicker.getValue());

        try {
            patronService.update(selectedPatronForEdit);
            NotifyDialogController.showNotification(
                    ((Node) actionEvent.getSource()).getScene().getWindow(),
                    "Patron was updated successfully!",
                    NotifyDialogController.NotificationType.SUCCESS
            );
        } catch (PatronNotFoundException | IllegalArgumentException e) {
            NotifyDialogController.showNotification(
                    ((Node) actionEvent.getSource()).getScene().getWindow(),
                    e.getMessage(),
                    NotifyDialogController.NotificationType.ERROR
            );
        }
        loadAllPatrons();
        cancelEdit(null);
    }

    @FXML
    public void cancelEdit(ActionEvent actionEvent) {
        selectedPatronForEdit = null;
        fullNameField.clear();
        cardIdField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        dateOfBirthPicker.setValue(null);
        registerDatePicker.setValue(null);
        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    public class PatronCell extends ListCell<PatronDto> {
        private final HBox hbox = new HBox();
        private final Label nameLabel = new Label();
        private final Button editButton = new Button("Edit");
        private final Pane spacer = new Pane();

        public PatronCell() {
            super();
            hbox.setSpacing(10);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hbox.getChildren().addAll(nameLabel, spacer, editButton);

            editButton.setOnAction(event -> {
                PatronDto patronDto = getItem();
                if (patronDto != null) {
                    selectedPatronForEdit = patronDto;
                    fullNameField.setText(patronDto.getFullName());
                    cardIdField.setText(patronDto.getCardId());
                    emailField.setText(patronDto.getEmail());
                    phoneField.setText(patronDto.getPhone());
                    addressField.setText(patronDto.getAddress());
                    dateOfBirthPicker.setValue(patronDto.getDateOfBirth());
                    registerDatePicker.setValue(patronDto.getRegisterDate());
                    editPanel.setVisible(true);
                    editPanel.setManaged(true);
                }
            });
        }

        @Override
        protected void updateItem(PatronDto patronDto, boolean b) {
            super.updateItem(patronDto, b);
            setText(null);
            if (b || patronDto == null) {
                setGraphic(null);
            } else {
                nameLabel.setText(patronDto.toString());
                setGraphic(hbox);
            }
        }
    }
}
