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
import lombok.RequiredArgsConstructor;
import org.abrohamovich.controller.MainController;
import org.abrohamovich.controller.NotifyDialogController;
import org.abrohamovich.dto.*;
import org.abrohamovich.entity.Format;
import org.abrohamovich.exceptions.BookNotFoundException;
import org.abrohamovich.service.interfaces.*;

import java.util.HashSet;

@RequiredArgsConstructor
public class ManageBookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;

    @FXML
    public TextField filterTextField;
    @FXML
    public ListView<BookDto> bookListView;
    @FXML
    public VBox editPanel;
    @FXML
    public TextField editBookTitleField;
    @FXML
    public TextField editIsbnField;
    @FXML
    public TextField editLanguageField;
    @FXML
    public Spinner<Integer> editNumberOfPagesSpinner;
    @FXML
    public ListView<AuthorDto> editAuthorListView;
    @FXML
    public ListView<GenreDto> editGenreListView;
    @FXML
    public ListView<CategoryDto> editCategoryListView;
    @FXML
    public ComboBox<PublisherDto> editPublisherComboBox;
    @FXML
    public ComboBox<Format> editFormatComboBox;

    private ObservableList<BookDto> books;
    private BookDto selectedBookForEdit;

    @FXML
    public void initialize() {
        books = FXCollections.observableArrayList();
        bookListView.setItems(books);
        bookListView.setCellFactory(lv ->
                new ManageBookController.BookCell());

        loadAllBooks();

        editNumberOfPagesSpinner.setEditable(true);

        editFormatComboBox.setItems(FXCollections.observableArrayList(Format.values()));
        editPublisherComboBox.setItems(FXCollections.observableArrayList(publisherService.findAll()));

        editAuthorListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        editGenreListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        editCategoryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        editAuthorListView.setItems(FXCollections.observableArrayList(authorService.findAll()));
        editCategoryListView.setItems(FXCollections.observableArrayList(categoryService.findAll()));
        editGenreListView.setItems(FXCollections.observableArrayList(genreService.findAll()));

        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    @FXML
    public void returnToMainMenu(MouseEvent mouseEvent) {
        MainController.loadMainMenuScene();
    }

    @FXML
    public void filterBooks(KeyEvent keyEvent) {
        String filterText = filterTextField.getText();
        if (filterText.isBlank()) {
            loadAllBooks();
        } else {
            books.clear();
            books.addAll(bookService.findByTitle(filterText));
        }
    }

    private void loadAllBooks() {
        books.clear();
        books.addAll(bookService.findAll());
    }

    @FXML
    public void saveBook(ActionEvent actionEvent) {
        if (selectedBookForEdit == null) return;

        selectedBookForEdit.setTitle(editBookTitleField.getText().trim());
        selectedBookForEdit.setIsbn(editIsbnField.getText().trim());
        selectedBookForEdit.setLanguage(editLanguageField.getText().trim());
        selectedBookForEdit.setNumberOfPages(editNumberOfPagesSpinner.getValue());
        selectedBookForEdit.setAuthors(new HashSet<>(editAuthorListView.getSelectionModel().getSelectedItems()));
        selectedBookForEdit.setGenres(new HashSet<>(editGenreListView.getSelectionModel().getSelectedItems()));
        selectedBookForEdit.setCategories(new HashSet<>(editCategoryListView.getSelectionModel().getSelectedItems()));
        selectedBookForEdit.setPublisher(editPublisherComboBox.getSelectionModel().getSelectedItem());
        selectedBookForEdit.setFormat(editFormatComboBox.getSelectionModel().getSelectedItem());

        try {
            bookService.update(selectedBookForEdit);
            NotifyDialogController.showNotification(
                    ((Node) actionEvent.getSource()).getScene().getWindow(),
                    "Book successfully updated",
                    NotifyDialogController.NotificationType.SUCCESS
            );
        } catch (BookNotFoundException | IllegalArgumentException e) {
            NotifyDialogController.showNotification(
                    ((Node) actionEvent.getSource()).getScene().getWindow(),
                    e.getMessage(),
                    NotifyDialogController.NotificationType.ERROR
            );
        }
        loadAllBooks();
        cancelEdit(null);
    }

    @FXML
    public void cancelEdit(ActionEvent actionEvent) {
        selectedBookForEdit = null;
        editGenreListView.setItems(FXCollections.emptyObservableList());
        editCategoryListView.setItems(FXCollections.emptyObservableList());
        editPublisherComboBox.setItems(FXCollections.emptyObservableList());
        editAuthorListView.setItems(FXCollections.emptyObservableList());
        editBookTitleField.clear();
        editIsbnField.clear();
        editLanguageField.clear();
        editPanel.setVisible(false);
        editPanel.setManaged(false);
    }

    public class BookCell extends ListCell<BookDto> {
        private final HBox hbox = new HBox();
        private final Label nameLabel = new Label();
        private final Button editButton = new Button("Edit");
        private final Pane spacer = new Pane();

        public BookCell() {
            super();
            hbox.setSpacing(10);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hbox.getChildren().addAll(nameLabel, spacer, editButton);

            editButton.setOnAction(event -> {
                BookDto bookDto = getItem();
                if (bookDto != null) {
                    selectedBookForEdit = bookDto;

                    editBookTitleField.setText(bookDto.getTitle());
                    editIsbnField.setText(bookDto.getIsbn());
                    editLanguageField.setText(bookDto.getLanguage());
                    editNumberOfPagesSpinner.setValueFactory(
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(
                                    1, Integer.MAX_VALUE, bookDto.getNumberOfPages())
                    );
                    editFormatComboBox.setValue(bookDto.getFormat());
                    editPublisherComboBox.setValue(publisherService.findByBook(bookDto));

                    for (AuthorDto author : authorService.findByBook(bookDto)) {
                        editAuthorListView.getSelectionModel().select(author);
                    }

                    for (GenreDto genre : genreService.findByBook(bookDto)) {
                        editGenreListView.getSelectionModel().select(genre);
                    }

                    for (CategoryDto category : categoryService.findByBook(bookDto)) {
                        editCategoryListView.getSelectionModel().select(category);
                    }

                    editPanel.setVisible(true);
                    editPanel.setManaged(true);
                }
            });
        }

        @Override
        protected void updateItem(BookDto bookDto, boolean b) {
            super.updateItem(bookDto, b);
            setText(null);
            if (b || bookDto == null) {
                setGraphic(null);
            } else {
                nameLabel.setText(bookDto.toString());
                setGraphic(hbox);
            }
        }
    }
}