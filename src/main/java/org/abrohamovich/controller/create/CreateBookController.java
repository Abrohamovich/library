package org.abrohamovich.controller.create;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.controller.MainController;
import org.abrohamovich.controller.NotifyDialogController;
import org.abrohamovich.dto.*;
import org.abrohamovich.entity.Format;
import org.abrohamovich.entity.Status;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.service.interfaces.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CreateBookController {

    private final AuthorService authorService;
    private final GenreService genreService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;
    private final BookService bookService;

    @FXML
    public TextField bookTitleField;
    @FXML
    public TextField isbnField;
    @FXML
    public TextField languageField;
    @FXML
    public Spinner<Integer> numberOfPagesSpinner;
    @FXML
    public ListView<AuthorDto> authorListView;
    @FXML
    public ListView<GenreDto> genreListView;
    @FXML
    public ListView<CategoryDto> categoryListView;
    @FXML
    public ComboBox<Format> formatComboBox;
    @FXML
    public ComboBox<PublisherDto> publisherComboBox;
    @FXML
    public DatePicker receiptDatePicker;

    @FXML
    private Button createAuthorButton;
    @FXML
    private Button createGenreButton;
    @FXML
    private Button createCategoryButton;
    @FXML
    private Button createPublisherButton;

    public CreateBookController(AuthorService authorService, GenreService genreService, CategoryService categoryService,
                                PublisherService publisherService, BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
        this.bookService = bookService;
    }

    @FXML
    public void initialize() {
        numberOfPagesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2500, 100));
        receiptDatePicker.setValue(LocalDate.now());

        bookTitleField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[-`.,:;?!'\\p{L}\\p{M} ]*")) {
                bookTitleField.setText(oldValue);
            }
        });

        isbnField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[-\\d]*")) {
                isbnField.setText(oldValue);
            }

            if(newValue.length()>13) {
                isbnField.setText(oldValue);
            }
        });

        languageField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[-`'\\p{L}\\p{M} ]*")) {
                languageField.setText(oldValue);
            }
        });

        receiptDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
           if (!newValue.equals(LocalDate.now())) {
               receiptDatePicker.setValue(LocalDate.now());
           }
        });

        authorListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        genreListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        categoryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        populateListsAndComboBoxes();

        formatComboBox.getSelectionModel().selectFirst();

    }

    private void populateListsAndComboBoxes() {
        authorListView.setItems(FXCollections.observableArrayList(authorService.findAll()));
        genreListView.setItems(FXCollections.observableArrayList(genreService.findAll()));
        categoryListView.setItems(FXCollections.observableArrayList(categoryService.findAll()));
        publisherComboBox.setItems(FXCollections.observableArrayList(publisherService.findAll()));

        formatComboBox.setItems(FXCollections.observableArrayList(Format.values()));
    }

    @FXML
    public void createBook(ActionEvent actionEvent) {
        String title = bookTitleField.getText();
        String isbn = isbnField.getText();
        String language = languageField.getText();
        int numberOfPages = numberOfPagesSpinner.getValue();
        Set<AuthorDto> selectedAuthors = new HashSet<>(authorListView.getSelectionModel().getSelectedItems());
        Set<GenreDto> selectedGenres = new HashSet<>(genreListView.getSelectionModel().getSelectedItems());
        Set<CategoryDto> selectedCategories = new HashSet<>(categoryListView.getSelectionModel().getSelectedItems());
        PublisherDto selectedPublisher = publisherComboBox.getValue();
        Format selectedFormat = formatComboBox.getValue();
        LocalDate receiptDate = receiptDatePicker.getValue();

        if (title == null || title.isBlank()) { showError("Book title cannot be empty."); return; }
        if (isbn == null || isbn.isBlank()) { showError("ISBN cannot be empty."); return; }
        if (language == null || language.isBlank()) { showError("Language cannot be empty."); return; }
        if (numberOfPages <= 0) { showError("Number of pages must be greater than 0."); return; }
        if (selectedAuthors.isEmpty()) { showError("Please select at least one Author."); return; }
        if (selectedGenres.isEmpty()) { showError("Please select at least one Genre."); return; }
        if (selectedCategories.isEmpty()) { showError("Please select at least one Category."); return; }
        if (selectedPublisher == null) { showError("Please select a Publisher."); return; }

        BookDto bookDto = BookDto.builder()
                .title(title)
                .isbn(isbn)
                .language(language)
                .numberOfPages(numberOfPages)
                .authors(selectedAuthors)
                .genres(selectedGenres)
                .categories(selectedCategories)
                .publisher(selectedPublisher)
                .status(Status.AVAILABLE)
                .format(selectedFormat)
                .receiptDate(receiptDate)
                .build();

        try {
            BookDto savedBookDto = bookService.save(bookDto);

            clearFields();

            NotifyDialogController.showNotification(
                    ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow(),
                    "Book '" + savedBookDto.getTitle() + "' successfully created!",
                    NotifyDialogController.NotificationType.SUCCESS
            );
        } catch (EntityException e) {
            showError("Error saving Book: " + e.getMessage());
        }

    }

    @FXML
    public void onCreateAuthor(ActionEvent actionEvent) {
        openCreationWindow("/create/create-author.fxml", "Library Application - Create Author", actionEvent);
    }

    @FXML
    public void onCreateGenre(ActionEvent actionEvent) {
        openCreationWindow("/create/create-genre.fxml", "Library Application - Create Genre", actionEvent);
    }

    @FXML
    public void onCreateCategory(ActionEvent actionEvent) {
        openCreationWindow("/create/create-category.fxml", "Library Application - Create Category", actionEvent);
    }

    @FXML
    public void onCreatePublisher(ActionEvent actionEvent) {
        openCreationWindow("/create/create-publisher.fxml", "Library Application - Create Publisher", actionEvent);
    }

    private void openCreationWindow(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            if (fxmlPath.contains("author")) {
                loader.setControllerFactory(controlClass -> {
                    if (controlClass == CreateAuthorController.class) {
                        return new CreateAuthorController(authorService);
                    }
                    try {
                        return controlClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Could not create controller", e);
                    }
                });
            } else if (fxmlPath.contains("genre")) {
                loader.setControllerFactory(controlClass -> {
                    if (controlClass == CreateGenreController.class) {
                        return new CreateGenreController(genreService);
                    }
                    try {
                        return controlClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Could not create controller", e);
                    }
                });
            } else if (fxmlPath.contains("category")) {
                loader.setControllerFactory(controlClass -> {
                    if (controlClass == CreateCategoryController.class) {
                        return new CreateCategoryController(categoryService);
                    }
                    try {
                        return controlClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Could not create controller", e);
                    }
                });
            } else if (fxmlPath.contains("publisher")) {
                loader.setControllerFactory(controlClass -> {
                    if (controlClass == CreatePublisherController.class) {
                        return new CreatePublisherController(publisherService);
                    }
                    try {
                        return controlClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Could not create controller", e);
                    }
                });
            }

            Parent parent = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            stage.setOnHidden(e -> populateListsAndComboBoxes());
            stage.showAndWait();

        } catch (IOException e) {
            log.error("Failed to load creation window for {}: {}", title, e.getMessage());
            NotifyDialogController.showNotification(
                    ((javafx.scene.Node) event.getSource()).getScene().getWindow(),
                    "Error opening creation window: " + e.getMessage(),
                    NotifyDialogController.NotificationType.ERROR
            );
        }
    }

    private void clearFields() {
        bookTitleField.clear();
        isbnField.clear();
        languageField.clear();
    }

    @FXML
    public void cancelCreation(ActionEvent actionEvent) {
        MainController.loadMainMenuScene();
    }

    private void showError(String message) {
        NotifyDialogController.showNotification(
                receiptDatePicker.getScene().getWindow(),
                "Input error: " + message,
                NotifyDialogController.NotificationType.ERROR
        );
    }
}
