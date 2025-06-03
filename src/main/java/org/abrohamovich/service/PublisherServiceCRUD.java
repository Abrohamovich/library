package org.abrohamovich.service;

import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.dto.BookDto;
import org.abrohamovich.dto.PublisherDto;
import org.abrohamovich.entity.Publisher;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.PublisherAlreadyExistException;
import org.abrohamovich.exceptions.PublisherNotFoundException;
import org.abrohamovich.mapper.PublisherMapper;
import org.abrohamovich.repository.PublisherRepository;
import org.abrohamovich.service.interfaces.PublisherService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PublisherServiceCRUD implements PublisherService {
    private final PublisherRepository repository;
    private final PublisherMapper mapper;

    public PublisherServiceCRUD(PublisherRepository repository, PublisherMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        log.info("PublisherServiceDefault initialized with repository: {} and mapper: {}", repository.getClass().getSimpleName(), mapper.getClass().getSimpleName());
    }

    @Override
    public PublisherDto save(PublisherDto publisherDto) throws PublisherAlreadyExistException, EntityException, IllegalArgumentException {
        if (publisherDto == null) {
            log.error("Invalid argument: null publisherDto");
            throw new IllegalArgumentException("Invalid argument: null publisherDto");
        }
        validatePublisherDto(publisherDto);
        log.debug("Attempting to save publisher with name: {}", publisherDto.getName());
        if (repository.findByName(publisherDto.getName()).isPresent()) {
            log.warn("Publisher with name '{}' already exists", publisherDto.getName());
            throw new PublisherAlreadyExistException("Trying to save persisted publisher with name " + publisherDto.getName());
        }
        Optional<Publisher> publisher = repository.save(mapper.toEntity(publisherDto));
        if (publisher.isEmpty()) {
            log.error("Failed to save publisher with name: {}", publisherDto.getName());
            throw new EntityException("Something went wrong while saving " + publisherDto.getName());
        }
        log.info("Successfully saved publisher with name: {}", publisherDto.getName());
        return mapper.toDto(publisher.get());
    }

    @Override
    public PublisherDto findById(long id) throws PublisherNotFoundException {
        log.debug("Searching for publisher with id: {}", id);
        Optional<Publisher> publisher = repository.findById(id);
        if (publisher.isEmpty()) {
            log.warn("Publisher with id {} not found", id);
            throw new PublisherNotFoundException("Trying to get publisher with id " + id);
        }
        log.info("Found publisher with id: {}", id);
        return mapper.toDto(publisher.get());
    }

    @Override
    public PublisherDto findByName(String name) throws PublisherNotFoundException, IllegalArgumentException {
        if (name.isBlank()) {
            log.error("Invalid argument: blank name");
            throw new IllegalArgumentException("Invalid argument: blank name");
        }
        log.debug("Searching for publisher with name: {}", name);
        Optional<Publisher> publisher = repository.findByName(name);
        if (publisher.isEmpty()) {
            log.warn("Publisher with name '{}' not found", name);
            throw new PublisherNotFoundException("Trying to get publisher with name " + name);
        }
        log.info("Found publisher with name: {}", name);
        return mapper.toDto(publisher.get());
    }

    @Override
    public List<PublisherDto> findByNameLike(String name) throws IllegalArgumentException {
        if (name.isBlank()) {
            log.error("Invalid argument: blank name");
            throw new IllegalArgumentException("Invalid argument: blank name");
        }
        log.debug("Searching for publisher with name: {}", name);
        List<Publisher> publishers = repository.findByNameLike(name);
        log.info("Found {} publishers with name: {}", publishers.size(), name);
        return publishers.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public PublisherDto findByEmail(String email) throws PublisherNotFoundException, IllegalArgumentException {
        if (email.isBlank()) {
            log.error("Invalid argument: blank email");
            throw new IllegalArgumentException("Invalid argument: blank email");
        }
        log.debug("Searching for publisher with email: {}", email);
        Optional<Publisher> publisher = repository.findByEmail(email);
        if (publisher.isEmpty()) {
            log.warn("Publisher with email '{}' not found", email);
            throw new PublisherNotFoundException("Trying to get publisher with email " + email);
        }
        log.info("Found publisher with email: {}", email);
        return mapper.toDto(publisher.get());
    }

    @Override
    public PublisherDto findByWebsite(String website) throws PublisherNotFoundException, IllegalArgumentException {
        if (website.isBlank()) {
            log.error("Invalid argument: blank website");
            throw new IllegalArgumentException("Invalid argument: blank website");
        }
        log.debug("Searching for publisher with website: {}", website);
        Optional<Publisher> publisher = repository.findByWebsite(website);
        if (publisher.isEmpty()) {
            log.warn("Publisher with website '{}' not found", website);
            throw new PublisherNotFoundException("Trying to get publisher with website " + website);
        }
        log.info("Found publisher with website: {}", website);
        return mapper.toDto(publisher.get());
    }

    @Override
    public PublisherDto findByAddress(String address) throws PublisherNotFoundException, IllegalArgumentException {
        if (address.isBlank()) {
            log.error("Invalid argument: blank address");
            throw new IllegalArgumentException("Invalid argument: blank address");
        }
        log.debug("Searching for publisher with address: {}", address);
        Optional<Publisher> publisher = repository.findByAddress(address);
        if (publisher.isEmpty()) {
            log.warn("Publisher with address '{}' not found", address);
            throw new PublisherNotFoundException("Trying to get publisher with address " + address);
        }
        log.info("Found publisher with address: {}", address);
        return mapper.toDto(publisher.get());
    }

    @Override
    public PublisherDto findByBook(BookDto bookDto) {
        log.debug("Searching for publisher with book title: {}", bookDto.getTitle());
        Optional<Publisher> publisher = repository.findByBookId(bookDto.getId());
        if (publisher.isEmpty()) {
            log.warn("Publisher with book title '{}' not found", bookDto.getTitle());
            throw new PublisherNotFoundException("Trying to get publisher with book title " + bookDto.getTitle());
        }
        log.info("Found publisher with book title: {}", bookDto.getTitle());
        return mapper.toDto(publisher.get());
    }

    @Override
    public List<PublisherDto> findAll() {
        log.debug("Fetching all publishers");
        List<Publisher> publishers = repository.findAll();
        log.info("Retrieved {} publishers", publishers.size());
        return publishers.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public PublisherDto update(PublisherDto publisherDto) throws PublisherNotFoundException, IllegalArgumentException {
        if (publisherDto == null) {
            log.error("Invalid argument: null publisherDto");
            throw new IllegalArgumentException("Invalid argument: null publisherDto");
        }
        validatePublisherDto(publisherDto);
        log.debug("Attempting to update publisher with id: {}", publisherDto.getId());
        Optional<Publisher> publisher = repository.update(mapper.toEntity(publisherDto));
        if (publisher.isEmpty()) {
            log.warn("Publisher with id {} not found for update", publisherDto.getId());
            throw new PublisherNotFoundException("Publisher with id " + publisherDto.getId() + " not found for update");
        }
        log.info("Successfully updated publisher with id: {}", publisherDto.getId());
        return mapper.toDto(publisher.get());
    }

    @Override
    public void delete(PublisherDto publisherDto) throws PublisherNotFoundException, IllegalArgumentException {
        if (publisherDto == null) {
            log.error("Invalid argument: null publisherDto");
            throw new IllegalArgumentException("Invalid argument: null publisherDto");
        }
        log.debug("Attempting to delete publisher with id: {}", publisherDto.getId());
        Optional<Publisher> publisher = repository.findById(publisherDto.getId());
        if (publisher.isEmpty()) {
            log.warn("Publisher with id {} not found for deletion", publisherDto.getId());
            throw new PublisherNotFoundException("Publisher with id " + publisherDto.getId() + " not found for delete");
        }
        repository.deleteById(publisherDto.getId());
        log.info("Successfully deleted publisher with id: {}", publisherDto.getId());
    }

    private void validatePublisherDto(PublisherDto publisherDto) {
        List<String> errors = new ArrayList<>();

        if (publisherDto.getName() == null || publisherDto.getName().isBlank()) {
            errors.add("Name is null or blank");
        }
        if (publisherDto.getAddress() != null && publisherDto.getAddress().isBlank()) {
            publisherDto.setAddress(null);
        }
        if (publisherDto.getEmail() != null && publisherDto.getEmail().isBlank()) {
            publisherDto.setEmail(null);
        }
        if (publisherDto.getWebsite() != null && publisherDto.getWebsite().isBlank()) {
            publisherDto.setWebsite(null);
        }
        if (publisherDto.getFoundationDate() == null) {
            errors.add("Foundation date is null");
        } else if (publisherDto.getFoundationDate().isAfter(LocalDate.now())) {
            errors.add("Foundation date cannot be in the future");
        }


        if (!errors.isEmpty()) {
            String errorMessage = String.join("; ", errors);
            log.error("Validation failed for publisher: {}. Errors: {}", publisherDto, errorMessage);
            throw new IllegalArgumentException("Validation failed: " + errorMessage);
        }
    }
}