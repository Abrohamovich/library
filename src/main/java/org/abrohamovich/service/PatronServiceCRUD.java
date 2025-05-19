package org.abrohamovich.service;

import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.dto.BookInstanceDto;
import org.abrohamovich.dto.PatronDto;
import org.abrohamovich.entity.Patron;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.PatronAlreadyExistException;
import org.abrohamovich.exceptions.PatronNotFoundException;
import org.abrohamovich.mapper.PatronMapper;
import org.abrohamovich.repository.PatronRepository;
import org.abrohamovich.service.interfaces.PatronService;

import java.util.List;
import java.util.Optional;

@Slf4j
public class PatronServiceCRUD implements PatronService {
    private final PatronRepository repository;
    private final PatronMapper mapper;

    public PatronServiceCRUD(PatronRepository repository, PatronMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        log.info("PatronServiceDefault initialized with repository: {} and mapper: {}", repository.getClass().getSimpleName(), mapper.getClass().getSimpleName());
    }

    @Override
    public PatronDto save(PatronDto patronDto) throws PatronAlreadyExistException, EntityException, IllegalArgumentException {
        if (patronDto == null) {
            log.error("Invalid argument: null patronDto");
            throw new IllegalArgumentException("Invalid argument: null patronDto");
        }

        log.info("Attempting to save patron with card ID: {}", patronDto.getCardId());
        if (repository.findByCardId(patronDto.getCardId()).isPresent()) {
            log.error("Patron with card ID {} already exists", patronDto.getCardId());
            throw new PatronAlreadyExistException("Trying to save persisted patron with card id " + patronDto.getCardId());
        }

        Optional<Patron> patron = repository.save(mapper.toEntity(patronDto));
        if (patron.isEmpty()) {
            log.error("Failed to save patron with card ID: {}", patronDto.getCardId());
            throw new EntityException("Something went wrong while saving patron with card id" + patronDto.getCardId());
        }

        log.info("Successfully saved patron with card ID: {}", patronDto.getCardId());
        return mapper.toDto(patron.get());
    }

    @Override
    public PatronDto findById(long id) throws PatronNotFoundException {
        log.info("Finding patron by ID: {}", id);
        Optional<Patron> patron = repository.findById(id);

        if (patron.isEmpty()) {
            log.error("Patron with ID {} not found", id);
            throw new PatronNotFoundException("Patron with id " + id + " not found");
        }

        log.info("Found patron with ID: {}", id);
        return mapper.toDto(patron.get());
    }

    @Override
    public PatronDto findByCardId(String cardId) throws PatronNotFoundException, IllegalArgumentException {
        if (cardId.isBlank()) {
            log.error("Invalid argument: blank cardId");
            throw new IllegalArgumentException("Invalid argument: blank cardId");
        }

        log.info("Finding patron by card ID: {}", cardId);
        Optional<Patron> patron = repository.findByCardId(cardId);

        if (patron.isEmpty()) {
            log.error("Patron with card ID {} not found", cardId);
            throw new PatronNotFoundException("Patron with card id" + cardId + " not found");
        }

        log.info("Found patron with card ID: {}", cardId);
        return mapper.toDto(patron.get());
    }

    @Override
    public PatronDto findByBookInstance(BookInstanceDto bookInstanceDto) throws PatronNotFoundException, IllegalArgumentException {
        if (bookInstanceDto == null) {
            log.error("Invalid argument: null bookInstanceDto");
            throw new IllegalArgumentException("Invalid argument: null bookInstanceDto");
        }

        log.info("Finding patron by book instance ID: {}", bookInstanceDto.getId());
        Optional<Patron> patron = repository.findByBookInstanceId(bookInstanceDto.getId());

        if (patron.isEmpty()) {
            log.error("Patron with book instance ID {} not found", bookInstanceDto.getId());
            throw new PatronNotFoundException("Patron with card id" + bookInstanceDto.getId() + " not found");
        }

        log.info("Found patron for book instance ID: {}", bookInstanceDto.getId());
        return mapper.toDto(patron.get());
    }

    @Override
    public List<PatronDto> findByFullName(String fullName) throws IllegalArgumentException {
        if (fullName.isBlank()) {
            log.error("Invalid argument: blank fullName");
            throw new IllegalArgumentException("Invalid argument: blank fullName");
        }

        log.info("Finding patrons by full name: {}", fullName);

        List<Patron> patrons = repository.findByFullName(fullName);
        log.info("Found {} patrons for full name: {}", patrons.size(), fullName);
        return patrons.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public PatronDto findByEmail(String email) throws PatronNotFoundException, IllegalArgumentException {
        if (email.isBlank()) {
            log.error("Invalid argument: blank email");
            throw new IllegalArgumentException("Invalid argument: blank email");
        }

        log.info("Finding patron by email: {}", email);
        Optional<Patron> patron = repository.findByEmail(email);

        if (patron.isEmpty()) {
            log.error("Patron with email {} not found", email);
            throw new PatronNotFoundException("Patron with card id" + email + " not found");
        }

        log.info("Found patron with email: {}", email);
        return mapper.toDto(patron.get());
    }

    @Override
    public PatronDto findByPhone(String phone) throws PatronNotFoundException, IllegalArgumentException {
        if (phone.isBlank()) {
            log.error("Invalid argument: blank phone");
            throw new IllegalArgumentException("Invalid argument: blank phone");
        }

        log.info("Finding patron by phone: {}", phone);
        Optional<Patron> patron = repository.findByPhone(phone);

        if (patron.isEmpty()) {
            log.error("Patron with phone {} not found", phone);
            throw new PatronNotFoundException("Patron with card id" + phone + " not found");
        }

        log.info("Found patron with phone: {}", phone);
        return mapper.toDto(patron.get());
    }

    @Override
    public PatronDto findByAddress(String address) throws PatronNotFoundException, IllegalArgumentException {
        if (address.isBlank()) {
            log.error("Invalid argument: blank address");
            throw new IllegalArgumentException("Invalid argument: blank address");
        }

        log.info("Finding patron by address: {}", address);
        Optional<Patron> patron = repository.findByAddress(address);

        if (patron.isEmpty()) {
            log.error("Patron with address {} not found", address);
            throw new PatronNotFoundException("Patron with card id" + address + " not found");
        }

        log.info("Found patron with address: {}", address);
        return mapper.toDto(patron.get());
    }

    @Override
    public List<PatronDto> findAll() {
        log.info("Finding all patrons");
        List<Patron> patrons = repository.findAll();
        log.info("Found {} patrons", patrons.size());
        return patrons.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public PatronDto update(PatronDto patronDto) throws PatronNotFoundException, IllegalArgumentException {
        if (patronDto == null) {
            log.error("Invalid argument: null patronDto");
            throw new IllegalArgumentException("Invalid argument: null patronDto");
        }

        log.info("Updating patron with ID: {}", patronDto.getId());
        Optional<Patron> patron = repository.update(mapper.toEntity(patronDto));
        if (patron.isEmpty()) {
            log.error("Patron with ID {} not found for update", patronDto.getId());
            throw new PatronNotFoundException("Patron with id " + patronDto.getId() + " is not found");
        }

        log.info("Successfully updated patron with ID: {}", patronDto.getId());
        return mapper.toDto(patron.get());
    }

    @Override
    public void delete(PatronDto patronDto) throws PatronNotFoundException, IllegalArgumentException {
        if (patronDto == null) {
            log.error("Invalid argument: null patronDto");
            throw new IllegalArgumentException("Invalid argument: null patronDto");
        }

        log.info("Attempting to delete patron with ID: {}", patronDto.getId());
        Optional<Patron> category = repository.findById(patronDto.getId());
        if (category.isEmpty()) {
            log.error("Patron with ID {} not found for deletion", patronDto.getId());
            throw new PatronNotFoundException("Patron with id " + patronDto.getId() + " is not found");
        }

        repository.deleteById(patronDto.getId());
        log.info("Successfully deleted patron with ID: {}", patronDto.getId());
    }
}