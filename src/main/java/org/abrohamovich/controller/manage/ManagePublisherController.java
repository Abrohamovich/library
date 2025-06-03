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
import org.abrohamovich.dto.PublisherDto;
import org.abrohamovich.exceptions.PublisherNotFoundException;
import org.abrohamovich.service.interfaces.PublisherService;

import java.util.List;

public class ManagePublisherController {

    private final PublisherService publisherService;
    @FXML
    public TextField filterTextField;
    @FXML
    public ListView<PublisherDto> publisherListView;
    @FXML
    public VBox editPanel;
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
    private ObservableList<PublisherDto> publishers;
    private PublisherDto selectedPublisherForEdit;

    public ManagePublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @FXML
    public void initialize() {
        publishers = FXCollections.observableArrayList();
        publisherListView.setItems(publishers);
        publisherListView.setCellFactory(lb ->
                new ManagePublisherController.PublisherCell());
        loadAllPublishers();

        nameField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[-`'\\p{L}\\p{M} ]*")) {
                nameField.setText(oldValue);
            }
        });

        addressField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[-`'\\d\\p{L}\\p{M} ]*")) {
                addressField.setText(oldValue);
            }
        });

        emailField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$")) {
                emailField.setText(oldValue);
            }
        });

        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    @FXML
    public void filterPublishers(KeyEvent keyEvent) {
        String filterName = filterTextField.getText().trim();
        if (filterName.isEmpty()) {
            loadAllPublishers();
        } else {
            List<PublisherDto> foundAuthors = publisherService.findByNameLike(filterName);
            publishers.clear();
            publishers.addAll(foundAuthors);
        }
    }

    private void loadAllPublishers() {
        publishers.clear();
        List<PublisherDto> allPublishers = publisherService.findAll();
        publishers.addAll(allPublishers);
    }

    @FXML
    public void returnToMainMenu(MouseEvent mouseEvent) {
        MainController.loadMainMenuScene();
    }

    @FXML
    public void savePublisher(ActionEvent actionEvent) {
        if (selectedPublisherForEdit == null) return;

        selectedPublisherForEdit.setName(nameField.getText().trim());
        selectedPublisherForEdit.setFoundationDate(foundationDatePicker.getValue());
        selectedPublisherForEdit.setAddress(addressField.getText().trim());
        selectedPublisherForEdit.setEmail(emailField.getText().trim());
        selectedPublisherForEdit.setWebsite(websiteField.getText().trim());

        try {
            publisherService.update(selectedPublisherForEdit);
            NotifyDialogController.showNotification(
                    ((Node) actionEvent.getSource()).getScene().getWindow(),
                    "Publisher was successfully updated!",
                    NotifyDialogController.NotificationType.ERROR
            );
        } catch (PublisherNotFoundException | IllegalArgumentException e) {
            NotifyDialogController.showNotification(
                    ((Node) actionEvent.getSource()).getScene().getWindow(),
                    e.getMessage(),
                    NotifyDialogController.NotificationType.ERROR
            );
        }
        loadAllPublishers();
        cancelEdit(null);
    }

    @FXML
    public void cancelEdit(ActionEvent actionEvent) {
        selectedPublisherForEdit = null;
        nameField.clear();
        foundationDatePicker.setValue(null);
        addressField.clear();
        emailField.clear();
        websiteField.clear();
        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    public class PublisherCell extends ListCell<PublisherDto> {
        private final HBox hbox = new HBox();
        private final Label nameLabel = new Label();
        private final Button editButton = new Button("Edit");
        private final Pane spacer = new Pane();

        public PublisherCell() {
            super();
            hbox.setSpacing(10);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hbox.getChildren().addAll(nameLabel, spacer, editButton);

            editButton.setOnAction(event -> {
                PublisherDto publisherDto = getItem();
                if (publisherDto != null) {
                    selectedPublisherForEdit = publisherDto;
                    nameField.setText(publisherDto.getName());
                    foundationDatePicker.setValue(publisherDto.getFoundationDate());
                    addressField.setText(publisherDto.getAddress());
                    emailField.setText(publisherDto.getEmail());
                    websiteField.setText(publisherDto.getWebsite());
                    editPanel.setVisible(true);
                    editPanel.setManaged(true);
                }
            });
        }

        @Override
        protected void updateItem(PublisherDto publisherDto, boolean b) {
            super.updateItem(publisherDto, b);
            setText(null);
            if (b || publisherDto == null) {
                setGraphic(null);
            } else {
                nameLabel.setText(publisherDto.toString());
                setGraphic(hbox);
            }
        }
    }

}
