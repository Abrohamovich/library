package org.abrohamovich.service;

import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.dto.GenreDto;
import org.abrohamovich.entity.Genre;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.GenreAlreadyExistException;
import org.abrohamovich.exceptions.GenreNotFoundException;
import org.abrohamovich.mapper.GenreMapper;
import org.abrohamovich.repository.GenreRepository;
import org.abrohamovich.service.interfaces.GenreService;

import java.util.List;
import java.util.Optional;

@Slf4j
public class GenreServiceCRUD implements GenreService {
    private final GenreRepository repository;
    private final GenreMapper mapper;

    public GenreServiceCRUD(GenreRepository repository, GenreMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        log.info("GenreServiceDefault initialized with repository: {} and mapper: {}", repository.getClass().getSimpleName(), mapper.getClass().getSimpleName());
    }

    @Override
    public GenreDto save(GenreDto genreDto) throws GenreAlreadyExistException, EntityException, IllegalArgumentException {
        if (genreDto == null) {
            log.error("Invalid argument: null genreDto");
            throw new IllegalArgumentException("Invalid argument: null genreDto");
        }
        log.debug("Attempting to save genre with name: {}", genreDto.getName());
        if (repository.findByName(genreDto.getName()).isPresent()) {
            log.warn("Genre with name '{}' already exists", genreDto.getName());
            throw new GenreAlreadyExistException("Trying to save persisted genre with name " + genreDto.getName());
        }
        Optional<Genre> genre = repository.save(mapper.toEntity(genreDto));
        if (genre.isEmpty()) {
            log.error("Failed to save genre with name: {}", genreDto.getName());
            throw new EntityException("Something went wrong while saving genre " + genreDto.getName());
        }
        log.info("Successfully saved genre with name: {}", genreDto.getName());
        return mapper.toDto(genre.get());
    }

    @Override
    public GenreDto findById(long id) throws GenreNotFoundException {
        log.debug("Searching for genre with id: {}", id);
        Optional<Genre> genre = repository.findById(id);
        if (genre.isEmpty()) {
            log.warn("Genre with id {} not found", id);
            throw new GenreNotFoundException("Genre not found with id " + id);
        }
        log.info("Found genre with id: {}", id);
        return mapper.toDto(genre.get());
    }

    @Override
    public GenreDto findByName(String name) throws GenreNotFoundException, IllegalArgumentException {
        if (name.isBlank()) {
            log.error("Invalid argument: blank name");
            throw new IllegalArgumentException("Invalid argument: blank name");
        }
        log.debug("Searching for genre with name: {}", name);
        Optional<Genre> genre = repository.findByName(name);
        if (genre.isEmpty()) {
            log.warn("Genre with name '{}' not found", name);
            throw new GenreNotFoundException("Genre not found with name " + name);
        }
        log.info("Found genre with name: {}", name);
        return mapper.toDto(genre.get());
    }

    @Override
    public List<GenreDto> findByNameLike(String name) throws IllegalArgumentException {
        if (name.isBlank()) {
            log.error("Invalid argument: blank name");
            throw new IllegalArgumentException("Invalid argument: blank name");
        }
        log.debug("Searching for genre with name: {}", name);
        List<Genre> genres = repository.findByNameLike(name);
        log.info("Found {} genres with name: {}", genres.size(), name);
        return genres.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<GenreDto> findAll() {
        log.debug("Fetching all genres");
        List<Genre> genres = repository.findAll();
        log.info("Retrieved {} genres", genres.size());
        return genres.stream().map(mapper::toDto).toList();
    }

    @Override
    public GenreDto update(GenreDto genreDto) throws GenreNotFoundException, IllegalArgumentException {
        if (genreDto == null) {
            log.error("Invalid argument: null genreDto");
            throw new IllegalArgumentException("Invalid argument: null genreDto");
        }
        log.debug("Attempting to update genre with id: {}", genreDto.getId());
        Optional<Genre> genre = repository.update(mapper.toEntity(genreDto));
        if (genre.isEmpty()) {
            log.warn("Genre with id {} not found for update", genreDto.getId());
            throw new GenreNotFoundException("Genre with id " + genreDto.getId() + " is not found");
        }
        log.info("Successfully updated genre with id: {}", genreDto.getId());
        return mapper.toDto(genre.get());
    }

    @Override
    public void delete(GenreDto genreDto) throws GenreNotFoundException, IllegalArgumentException {
        if (genreDto == null) {
            log.error("Invalid argument: null genreDto");
            throw new IllegalArgumentException("Invalid argument: null genreDto");
        }
        log.debug("Attempting to delete genre with id: {}", genreDto.getId());
        Optional<Genre> genre = repository.findById(genreDto.getId());
        if (genre.isEmpty()) {
            log.warn("Genre with id {} not found for deletion", genreDto.getId());
            throw new GenreNotFoundException("Genre with id " + genreDto.getId() + " is not found");
        }
        repository.deleteById(genreDto.getId());
        log.info("Successfully deleted genre with id: {}", genreDto.getId());
    }
}
