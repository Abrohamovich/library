package org.abrohamovich.service;

import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.dto.*;
import org.abrohamovich.entity.Book;
import org.abrohamovich.entity.Format;
import org.abrohamovich.entity.Status;
import org.abrohamovich.exceptions.BookNotFoundException;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.mapper.BookMapper;
import org.abrohamovich.repository.BookRepository;
import org.abrohamovich.service.interfaces.BookService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class BookServiceCRUD implements BookService {
    private final BookRepository repository;
    private final BookMapper mapper;

    public BookServiceCRUD(BookRepository repository, BookMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        log.info("BookServiceDefault initialized with repository: {} and mapper: {}", repository.getClass().getSimpleName(), mapper.getClass().getSimpleName());
    }

    @Override
    public BookDto save(BookDto bookDto) throws EntityException, IllegalArgumentException {
        if (bookDto == null) {
            log.error("Invalid argument: null bookDto");
            throw new IllegalArgumentException("Invalid argument: null bookDto");
        }
        validateBookDto(bookDto);
        log.info("Attempting to save book with ISBN: {}", bookDto.getIsbn());
        Optional<Book> book = repository.save(mapper.toEntity(bookDto));
        if (book.isEmpty()) {
            log.error("Failed to save book with ISBN: {}", bookDto.getIsbn());
            throw new EntityException("Something went wrong while saving book with ISBN " + bookDto.getIsbn());
        }
        log.info("Successfully saved book with ISBN: {}", bookDto.getIsbn());
        return mapper.toDto(book.get());
    }

    @Override
    public BookDto findById(long id) throws BookNotFoundException {
        log.info("Finding book by ID: {}", id);
        Optional<Book> book = repository.findById(id);
        if (book.isEmpty()) {
            log.error("Book with ID {} not found", id);
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
        log.info("Found book with ID: {}", id);
        return mapper.toDto(book.get());
    }

    @Override
    public List<BookDto> findByTitle(String title) {
        log.debug("Searching for books with title: {}", title);
        List<Book> books = repository.findByTitle(title);
        log.info("Found {} books for title: {}", books.size(), title);
        return books.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findByAuthors(List<AuthorDto> authorDtos) throws IllegalArgumentException {
        if (authorDtos == null || authorDtos.isEmpty()) {
            log.error("Invalid argument: null authorDtos");
            throw new IllegalArgumentException("Invalid argument: null authorDtos");
        }
        log.info("Finding books by authors: {}", authorDtos);
        List<Long> list = authorDtos.stream().map(AuthorDto::getId).toList();
        List<Book> books = repository.findByAuthorIds(list);
        log.info("Found {} books for authors: {}", books.size(), list);
        return books.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findByIsbn(String isbn) throws IllegalArgumentException {
        if (isbn.isBlank()) {
            log.error("Invalid argument: blank ISBN");
            throw new IllegalArgumentException("Invalid argument: blank ISBN");
        }
        log.info("Finding books by ISBN: {}", isbn);
        List<Book> books = repository.findByIsbn(isbn);
        log.info("Found books with ISBN: {}", isbn);
        return books.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findByGenres(List<GenreDto> genreDtos) throws IllegalArgumentException {
        if (genreDtos == null || genreDtos.isEmpty()) {
            log.error("Invalid argument: null genreDtos");
            throw new IllegalArgumentException("Invalid argument: null genreDtos");
        }
        log.info("Finding books by genres: {}", genreDtos);
        List<Long> list = genreDtos.stream().map(GenreDto::getId).toList();
        List<Book> books = repository.findByGenreIds(list);
        log.info("Found {} books for genres: {}", books.size(), list);
        return books.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findByCategories(List<CategoryDto> categoryDtos) throws IllegalArgumentException {
        if (categoryDtos == null || categoryDtos.isEmpty()) {
            log.error("Invalid argument: null categoryDtos");
            throw new IllegalArgumentException("Invalid argument: null categoryDtos");
        }
        log.info("Finding books by categories: {}", categoryDtos);
        List<Long> list = categoryDtos.stream().map(CategoryDto::getId).toList();
        List<Book> books = repository.findByCategoryIds(list);
        log.info("Found {} books for categories: {}", books.size(), list);
        return books.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findByLanguage(String language) throws IllegalArgumentException {
        if (language.isBlank()) {
            log.error("Invalid argument: null or empty language");
            throw new IllegalArgumentException("Invalid argument: null or empty language");
        }
        log.info("Finding books by language: {}", language);
        List<Book> books = repository.findByLanguage(language);
        log.info("Found {} books for language: {}", books.size(), language);
        return books.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findByPublisher(PublisherDto publisherDto) throws IllegalArgumentException {
        if (publisherDto == null) {
            log.error("Invalid argument: null publisherDto");
            throw new IllegalArgumentException("Invalid argument: null publisherDto");
        }
        log.debug("Searching for book instances with publisher id: {}", publisherDto.getId());
        List<Book> bookInstances = repository.findByPublisherId(publisherDto.getId());
        log.info("Found {} book instances for publisher id: {}", bookInstances.size(), publisherDto.getId());
        return bookInstances.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findByStatus(Status status) throws IllegalArgumentException {
        if (status == null) {
            log.error("Invalid argument: null status");
            throw new IllegalArgumentException("Invalid argument: null status");
        }
        log.debug("Searching for book instances with status: {}", status);
        List<Book> bookInstances = repository.findByStatus(status);
        log.info("Found {} book instances with status: {}", bookInstances.size(), status);
        return bookInstances.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findByFormat(Format format) throws IllegalArgumentException {
        if (format == null) {
            log.error("Invalid argument: null format");
            throw new IllegalArgumentException("Invalid argument: null format");
        }
        log.debug("Searching for book instances with format: {}", format);
        List<Book> bookInstances = repository.findByFormat(format);
        log.info("Found {} book instances with format: {}", bookInstances.size(), format);
        return bookInstances.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findAll() {
        log.info("Finding all books");
        List<Book> books = repository.findAll();
        log.info("Found {} books", books.size());

        return books.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public BookDto update(BookDto bookDto) throws BookNotFoundException, IllegalArgumentException {
        if (bookDto == null) {
            log.error("Invalid argument: null bookDto");
            throw new IllegalArgumentException("Invalid argument: null bookDto");
        }
        validateBookDto(bookDto);
        log.info("Updating book with ID: {}", bookDto.getId());
        Optional<Book> book = repository.update(mapper.toEntity(bookDto));
        if (book.isEmpty()) {
            log.error("Book with ID {} not found for update", bookDto.getId());
            throw new BookNotFoundException("Book with id " + bookDto.getId() + " is not found");
        }
        log.info("Successfully updated book with ID: {}", bookDto.getId());
        return mapper.toDto(book.get());
    }

    @Override
    public void delete(BookDto bookDto) throws BookNotFoundException, IllegalArgumentException {
        if (bookDto == null) {
            log.error("Invalid argument: null bookDto");
            throw new IllegalArgumentException("Invalid argument: null bookDto");
        }
        log.info("Attempting to delete book with ID: {}", bookDto.getId());
        Optional<Book> book = repository.findById(bookDto.getId());
        if (book.isEmpty()) {
            log.error("Book with ID {} not found for deletion", bookDto.getId());
            throw new BookNotFoundException("Book with id " + bookDto.getId() + " is not found");
        }
        repository.deleteById(bookDto.getId());
        log.info("Successfully deleted book with ID: {}", bookDto.getId());
    }

    private void validateBookDto(BookDto bookDto) {
        List<String> errors = new ArrayList<>();

        if (bookDto.getTitle() == null || bookDto.getTitle().isBlank()) {
            errors.add("Title is null or blank");
        }
        if (bookDto.getIsbn() == null || bookDto.getIsbn().isBlank()) {
            errors.add("ISBN is null or blank");
        }
        if (bookDto.getLanguage() == null || bookDto.getLanguage().isBlank()) {
            errors.add("Language is null or blank");
        }
        if (bookDto.getNumberOfPages() < 1) {
            errors.add("Number of pages is less than 1");
        }
        if (bookDto.getAuthors().isEmpty()) {
            errors.add("Authors cannot be empty");
        }
        if (bookDto.getGenres().isEmpty()) {
            errors.add("Genres cannot be empty");
        }
        if (bookDto.getCategories().isEmpty()) {
            errors.add("Categories cannot be empty");
        }
        if (bookDto.getFormat() == null) errors.add("Format cannot be null");
        if (bookDto.getStatus() == null) errors.add("Status cannot be null");
        if (bookDto.getPublisher() == null) errors.add("Publisher cannot be null");
        if (bookDto.getReceiptDate() == null) {
            errors.add("ReceiptDate cannot be null");
        } else if (bookDto.getReceiptDate().isAfter(LocalDate.now())) {
            errors.add("ReceiptDate cannot be in the future");
        }

        if (!errors.isEmpty()) {
            String errorMessage = String.join("; ", errors);
            log.error("Validation failed for book: {}. Errors: {}", bookDto.getTitle(), errorMessage);
            throw new IllegalArgumentException("Validation failed: " + errorMessage);
        }
    }
}