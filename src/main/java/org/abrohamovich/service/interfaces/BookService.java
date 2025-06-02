package org.abrohamovich.service.interfaces;

import org.abrohamovich.dto.*;
import org.abrohamovich.entity.Format;
import org.abrohamovich.entity.Status;
import org.abrohamovich.exceptions.BookNotFoundException;
import org.abrohamovich.exceptions.EntityException;

import java.util.List;

public interface BookService {

    BookDto save(BookDto bookDto)
            throws EntityException, IllegalArgumentException;

    BookDto findById(long id)
            throws BookNotFoundException;

    List<BookDto> findByTitle(String title);

    List<BookDto> findByAuthors(List<AuthorDto> authorDtos)
            throws IllegalArgumentException;

    List<BookDto> findByIsbn(String isbn)
            throws IllegalArgumentException;

    List<BookDto> findByGenres(List<GenreDto> genreDtos)
            throws IllegalArgumentException;

    List<BookDto> findByCategories(List<CategoryDto> categoryDtos)
            throws IllegalArgumentException;

    List<BookDto> findByLanguage(String language)
            throws IllegalArgumentException;

    List<BookDto> findByPublisher(PublisherDto publisherDto)
            throws IllegalArgumentException;

    List<BookDto> findByStatus(Status status)
            throws IllegalArgumentException;

    List<BookDto> findByFormat(Format format)
            throws IllegalArgumentException;

    List<BookDto> findAll();

    BookDto update(BookDto bookDto)
            throws BookNotFoundException, IllegalArgumentException;

    void delete(BookDto bookDto)
            throws BookNotFoundException, IllegalArgumentException;
}
