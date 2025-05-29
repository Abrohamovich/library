package org.abrohamovich.service;

import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.dto.AuthorDto;
import org.abrohamovich.entity.Author;
import org.abrohamovich.entity.Sex;
import org.abrohamovich.exceptions.AuthorNotFoundException;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.mapper.AuthorMapper;
import org.abrohamovich.repository.AuthorRepository;
import org.abrohamovich.service.interfaces.AuthorService;

import java.util.List;
import java.util.Optional;

@Slf4j
public class AuthorServiceCRUD implements AuthorService {
    private final AuthorRepository repository;
    private final AuthorMapper mapper;

    public AuthorServiceCRUD(AuthorRepository repository, AuthorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        log.info("AuthorServiceDefault initialized with repository: {} and mapper: {}", repository.getClass().getSimpleName(), mapper.getClass().getSimpleName());
    }

    @Override
    public AuthorDto save(AuthorDto authorDto) throws EntityException, IllegalArgumentException {
        if (authorDto == null) {
            log.error("Invalid argument: null author");
            throw new IllegalArgumentException("Invalid argument: null author");
        }
        log.debug("Attempting to save author with full name: {}", authorDto.getFullName());
        Optional<Author> author = repository.save(mapper.toEntity(authorDto));
        if (author.isEmpty()) {
            log.error("Failed to save author with full name: {}", authorDto.getFullName());
            throw new EntityException("Something went wrong while saving " + authorDto.getFullName());
        }
        log.info("Successfully saved author with full name: {}", authorDto.getFullName());
        return mapper.toDto(author.get());
    }

    @Override
    public AuthorDto findById(long id) throws AuthorNotFoundException {
        log.debug("Searching for author with id: {}", id);
        Optional<Author> author = repository.findById(id);
        if (author.isEmpty()) {
            log.warn("Author with id {} not found", id);
            throw new AuthorNotFoundException("Could not find author with id " + id);
        }
        log.info("Found author with id: {}", id);
        return mapper.toDto(author.get());
    }

    @Override
    public List<AuthorDto> findBySex(Sex sex) throws IllegalArgumentException {
        if (sex == null) {
            log.error("Invalid argument: sex is null");
            throw new IllegalArgumentException("Invalid argument: sex is null");
        }
        log.debug("Searching for authors with sex: {}", sex);
        List<Author> authors = repository.findBySex(sex);
        log.info("Found {} authors with sex: {}", authors.size(), sex);
        return authors.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<AuthorDto> findByFullName(String fullName) throws IllegalArgumentException {
        if (fullName.isBlank()) {
            log.error("Invalid argument: blank fullName");
            throw new IllegalArgumentException("Invalid argument: blank fullName");
        }
        log.debug("Searching for authors with full name: {}", fullName);
        List<Author> authors = repository.findByFullName(fullName);
        log.info("Found {} authors with full name: {}", authors.size(), fullName);
        return authors.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<AuthorDto> findByFullNameLike(String fullName) throws IllegalArgumentException {
        if (fullName.isBlank()) {
            log.error("Invalid argument: blank fullName");
            throw new IllegalArgumentException("Invalid argument: blank fullName");
        }
        log.debug("Searching for authors with full name: {}", fullName);
        List<Author> authors = repository.findByFullNameLike(fullName);
        log.info("Found {} authors with full name: {}", authors.size(), fullName);
        return authors.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<AuthorDto> findAll() {
        log.debug("Fetching all authors");
        List<Author> authors = repository.findAll();
        log.info("Retrieved {} authors", authors.size());
        return authors.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public AuthorDto update(AuthorDto authorDto) throws AuthorNotFoundException {
        if (authorDto == null) {
            log.error("Invalid argument: null author");
            throw new IllegalArgumentException("Invalid argument: null author");
        }
        log.debug("Attempting to update author with id: {}", authorDto.getId());
        Optional<Author> author = repository.update(mapper.toEntity(authorDto));
        if (author.isEmpty()) {
            log.warn("Author with id {} not found for update", authorDto.getId());
            throw new AuthorNotFoundException("Could not find author with id " + authorDto.getId());
        }
        log.info("Successfully updated author with id: {}", authorDto.getId());
        return mapper.toDto(author.get());
    }

    @Override
    public void delete(AuthorDto authorDto) throws AuthorNotFoundException {
        if (authorDto == null) {
            log.error("Invalid argument: null author");
            throw new IllegalArgumentException("Invalid argument: null author");
        }
        log.debug("Attempting to delete author with id: {}", authorDto.getId());
        Optional<Author> author = repository.findById(authorDto.getId());
        if (author.isEmpty()) {
            log.warn("Author with id {} not found for deletion", authorDto.getId());
            throw new AuthorNotFoundException("Could not find author with id " + authorDto.getId());
        }
        repository.deleteById(authorDto.getId());
        log.info("Successfully deleted author with id: {}", authorDto.getId());
    }
}