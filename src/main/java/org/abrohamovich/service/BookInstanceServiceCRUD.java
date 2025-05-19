package org.abrohamovich.service;

import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.dto.BookDto;
import org.abrohamovich.dto.BookInstanceDto;
import org.abrohamovich.dto.PublisherDto;
import org.abrohamovich.entity.BookInstance;
import org.abrohamovich.entity.Format;
import org.abrohamovich.entity.Status;
import org.abrohamovich.exceptions.BookInstanceNotFoundException;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.mapper.BookInstanceMapper;
import org.abrohamovich.repository.BookInstanceRepository;
import org.abrohamovich.service.interfaces.BookInstanceService;

import java.util.List;
import java.util.Optional;

@Slf4j
public class BookInstanceServiceCRUD implements BookInstanceService {
    private final BookInstanceRepository repository;
    private final BookInstanceMapper mapper;

    public BookInstanceServiceCRUD(BookInstanceRepository repository, BookInstanceMapper mapper) {
        this.mapper = mapper;
        this.repository = repository;
        log.info("BookInstanceServiceDefault initialized with repository: {} and mapper: {}", repository.getClass().getSimpleName(), mapper.getClass().getSimpleName());
    }

    @Override
    public BookInstanceDto save(BookInstanceDto bookInstanceDto) throws EntityException, IllegalArgumentException {
        if (bookInstanceDto == null) {
            log.error("Invalid argument: null bookInstanceDto");
            throw new IllegalArgumentException("Invalid argument: null bookInstanceDto");
        }
        log.debug("Attempting to save book instance for book id: {}", bookInstanceDto.getId());
        Optional<BookInstance> bookInstance = repository.save(mapper.toEntity(bookInstanceDto));
        if (bookInstance.isEmpty()) {
            log.error("Failed to save book instance for id: {}", bookInstanceDto.getId());
            throw new EntityException("Something went wrong while saving bookInstance");
        }
        log.info("Successfully saved book instance with id: {}", bookInstance.get().getId());
        return mapper.toDto(bookInstance.get());
    }

    @Override
    public BookInstanceDto findById(long id) throws BookInstanceNotFoundException {
        log.debug("Searching for book instance with id: {}", id);
        Optional<BookInstance> bookInstance = repository.findById(id);
        if (bookInstance.isEmpty()) {
            log.warn("Book instance with id {} not found", id);
            throw new BookInstanceNotFoundException("Could not find book instance with id " + id);
        }
        log.info("Found book instance with id: {}", id);
        return mapper.toDto(bookInstance.get());
    }

    @Override
    public List<BookInstanceDto> findByBook(BookDto bookDto) throws IllegalArgumentException {
        if (bookDto == null) {
            log.error("Invalid argument: null bookDto");
            throw new IllegalArgumentException("Invalid argument: null bookDto");
        }
        log.debug("Searching for book instances with book id: {}", bookDto.getId());
        List<BookInstance> bookInstances = repository.findByBookId(bookDto.getId());
        log.info("Found {} book instances for book id: {}", bookInstances.size(), bookDto.getId());
        return bookInstances.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookInstanceDto> findByPublisher(PublisherDto publisherDto) throws IllegalArgumentException {
        if (publisherDto == null) {
            log.error("Invalid argument: null publisherDto");
            throw new IllegalArgumentException("Invalid argument: null publisherDto");
        }
        log.debug("Searching for book instances with publisher id: {}", publisherDto.getId());
        List<BookInstance> bookInstances = repository.findByPublisherId(publisherDto.getId());
        log.info("Found {} book instances for publisher id: {}", bookInstances.size(), publisherDto.getId());
        return bookInstances.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookInstanceDto> findByStatus(Status status) throws IllegalArgumentException {
        if (status == null) {
            log.error("Invalid argument: null status");
            throw new IllegalArgumentException("Invalid argument: null status");
        }
        log.debug("Searching for book instances with status: {}", status);
        List<BookInstance> bookInstances = repository.findByStatus(status);
        log.info("Found {} book instances with status: {}", bookInstances.size(), status);
        return bookInstances.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookInstanceDto> findByFormat(Format format) throws IllegalArgumentException {
        if (format == null) {
            log.error("Invalid argument: null format");
            throw new IllegalArgumentException("Invalid argument: null format");
        }
        log.debug("Searching for book instances with format: {}", format);
        List<BookInstance> bookInstances = repository.findByFormat(format);
        log.info("Found {} book instances with format: {}", bookInstances.size(), format);
        return bookInstances.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookInstanceDto> findAll() {
        log.debug("Fetching all book instances");
        List<BookInstance> bookInstances = repository.findAll();
        log.info("Retrieved {} book instances", bookInstances.size());
        return bookInstances.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public BookInstanceDto update(BookInstanceDto bookInstanceDto) throws BookInstanceNotFoundException, IllegalArgumentException {
        if (bookInstanceDto == null) {
            log.error("Invalid argument: null bookInstanceDto");
            throw new IllegalArgumentException("Invalid argument: null bookInstanceDto");
        }
        log.debug("Attempting to update book instance with id: {}", bookInstanceDto.getId());
        Optional<BookInstance> bookInstance = repository.update(mapper.toEntity(bookInstanceDto));
        if (bookInstance.isEmpty()) {
            log.warn("Book instance with id {} not found for update", bookInstanceDto.getId());
            throw new BookInstanceNotFoundException("Could not find book instance with id " + bookInstanceDto.getId());
        }
        log.info("Successfully updated book instance with id: {}", bookInstanceDto.getId());
        return mapper.toDto(bookInstance.get());
    }

    @Override
    public void delete(BookInstanceDto bookInstanceDto) throws BookInstanceNotFoundException, IllegalArgumentException {
        if (bookInstanceDto == null) {
            log.error("Invalid argument: null bookInstanceDto");
            throw new IllegalArgumentException("Invalid argument: null bookInstanceDto");
        }
        log.debug("Attempting to delete book instance with id: {}", bookInstanceDto.getId());
        Optional<BookInstance> bookInstance = repository.findById(bookInstanceDto.getId());
        if (bookInstance.isEmpty()) {
            log.warn("Book instance with id {} not found for deletion", bookInstanceDto.getId());
            throw new BookInstanceNotFoundException("Could not find book instance with id " + bookInstanceDto.getId());
        }
        repository.deleteById(bookInstanceDto.getId());
        log.info("Successfully deleted book instance with id: {}", bookInstanceDto.getId());
    }
}