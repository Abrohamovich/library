package org.abrohamovich.service.interfaces;

import org.abrohamovich.dto.BookDto;
import org.abrohamovich.dto.CategoryDto;
import org.abrohamovich.exceptions.CategoryAlreadyExistException;
import org.abrohamovich.exceptions.CategoryNotFoundException;
import org.abrohamovich.exceptions.EntityException;

import java.util.List;

public interface CategoryService {

    CategoryDto save(CategoryDto categoryDto)
            throws CategoryAlreadyExistException, EntityException, IllegalArgumentException;

    CategoryDto findById(long id)
            throws CategoryNotFoundException;

    CategoryDto findByName(String name)
            throws CategoryNotFoundException, IllegalArgumentException;

    List<CategoryDto> findByNameLike(String name)
            throws IllegalArgumentException;

    List<CategoryDto> findByBook(BookDto bookDto);

    List<CategoryDto> findAll();

    CategoryDto update(CategoryDto categoryDto)
            throws CategoryNotFoundException, IllegalArgumentException;

    void delete(CategoryDto categoryDto)
            throws CategoryNotFoundException, IllegalArgumentException;
}
