package org.abrohamovich.service;

import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.dto.CategoryDto;
import org.abrohamovich.entity.Category;
import org.abrohamovich.exceptions.CategoryAlreadyExistException;
import org.abrohamovich.exceptions.CategoryNotFoundException;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.mapper.CategoryMapper;
import org.abrohamovich.repository.CategoryRepository;
import org.abrohamovich.service.interfaces.CategoryService;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CategoryServiceCRUD implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryServiceCRUD(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        log.info("CategoryServiceDefault initialized with repository: {} and mapper: {}", repository.getClass().getSimpleName(), mapper.getClass().getSimpleName());
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto)
            throws CategoryAlreadyExistException, EntityException, IllegalArgumentException {
        if (categoryDto == null) {
            log.error("Invalid argument: null categoryDto");
            throw new IllegalArgumentException("Invalid argument: null categoryDto");
        }
        log.debug("Attempting to save category with name: {}", categoryDto.getName());
        if (repository.findByName(categoryDto.getName()).isPresent()) {
            log.warn("Category with name '{}' already exists", categoryDto.getName());
            throw new CategoryAlreadyExistException("Trying to save persisted category with name " + categoryDto.getName());
        }
        Optional<Category> category = repository.save(mapper.toEntity(categoryDto));
        if (category.isEmpty()) {
            log.error("Failed to save category with name: {}", categoryDto.getName());
            throw new EntityException("Something went wrong while saving " + categoryDto.getName());
        }
        log.info("Successfully saved category with name: {}", categoryDto.getName());
        return mapper.toDto(category.get());
    }

    @Override
    public CategoryDto findById(long id) throws CategoryNotFoundException {
        log.debug("Searching for category with id: {}", id);
        Optional<Category> category = repository.findById(id);
        if (category.isEmpty()) {
            log.warn("Category with id {} not found", id);
            throw new CategoryNotFoundException("Category not found with id " + id);
        }
        log.info("Found category with id: {}", id);
        return mapper.toDto(category.get());
    }

    @Override
    public CategoryDto findByName(String name) throws CategoryNotFoundException, IllegalArgumentException {
        if (name.isBlank()) {
            log.error("Invalid argument: blank name");
            throw new IllegalArgumentException("Invalid argument: blank name");
        }
        log.debug("Searching for category with name: {}", name);
        Optional<Category> category = repository.findByName(name);
        if (category.isEmpty()) {
            log.warn("Category with name '{}' not found", name);
            throw new CategoryNotFoundException("Category not found with name " + name);
        }
        log.info("Found category with name: {}", name);
        return mapper.toDto(category.get());
    }

    @Override
    public List<CategoryDto> findAll() {
        log.debug("Fetching all categories");
        List<Category> categories = repository.findAll();
        log.info("Retrieved {} categories", categories.size());
        return categories.stream().map(mapper::toDto).toList();
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) throws CategoryNotFoundException, IllegalArgumentException {
        if (categoryDto == null) {
            log.error("Invalid argument: null categoryDto");
            throw new IllegalArgumentException("Invalid argument: null categoryDto");
        }
        log.debug("Attempting to update category with id: {}", categoryDto.getId());
        Optional<Category> category = repository.update(mapper.toEntity(categoryDto));
        if (category.isEmpty()) {
            log.warn("Category with id {} not found for update", categoryDto.getId());
            throw new CategoryNotFoundException("Category with id " + categoryDto.getId() + " is not found");
        }
        log.info("Successfully updated category with id: {}", categoryDto.getId());
        return mapper.toDto(category.get());
    }

    @Override
    public void delete(CategoryDto categoryDto) throws CategoryNotFoundException, IllegalArgumentException {
        if (categoryDto == null) {
            log.error("Invalid argument: null categoryDto");
            throw new IllegalArgumentException("Invalid argument: null categoryDto");
        }
        log.debug("Attempting to delete category with id: {}", categoryDto.getId());
        Optional<Category> category = repository.findById(categoryDto.getId());
        if (category.isEmpty()) {
            log.warn("Category with id {} not found for deletion", categoryDto.getId());
            throw new CategoryNotFoundException("Category with id " + categoryDto.getId() + " is not found");
        }
        repository.deleteById(categoryDto.getId());
        log.info("Successfully deleted category with id: {}", categoryDto.getId());
    }
}