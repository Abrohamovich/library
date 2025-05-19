package org.abrohamovich.service.interfaces;

import org.abrohamovich.dto.AuthorDto;
import org.abrohamovich.dto.BookDto;
import org.abrohamovich.dto.CategoryDto;
import org.abrohamovich.dto.GenreDto;
import org.abrohamovich.exceptions.BookAlreadyExistException;
import org.abrohamovich.exceptions.BookNotFoundException;
import org.abrohamovich.exceptions.EntityException;

import java.util.List;

public interface BookService {

    BookDto save(BookDto bookDto)
            throws BookAlreadyExistException, EntityException, IllegalArgumentException;

    BookDto findById(long id)
            throws BookNotFoundException;

    List<BookDto> findByAuthors(List<AuthorDto> authorDtos)
            throws IllegalArgumentException;

    BookDto findByIsbn(String isbn)
            throws BookNotFoundException, IllegalArgumentException;

    List<BookDto> findByGenres(List<GenreDto> genreDtos)
            throws IllegalArgumentException;

    List<BookDto> findByCategories(List<CategoryDto> categoryDtos)
            throws IllegalArgumentException;

    List<BookDto> findByLanguage(String language)
            throws IllegalArgumentException;

    List<BookDto> findAll();

    BookDto update(BookDto bookDto)
            throws BookNotFoundException, IllegalArgumentException;

    void delete(BookDto bookDto)
            throws BookNotFoundException, IllegalArgumentException;
}
