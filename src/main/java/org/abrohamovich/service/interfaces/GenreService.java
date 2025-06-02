package org.abrohamovich.service.interfaces;

import org.abrohamovich.dto.BookDto;
import org.abrohamovich.dto.GenreDto;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.GenreAlreadyExistException;
import org.abrohamovich.exceptions.GenreNotFoundException;

import java.util.List;

public interface GenreService {

    GenreDto save(GenreDto genreDto)
            throws GenreAlreadyExistException, EntityException, IllegalArgumentException;

    GenreDto findById(long id)
            throws GenreNotFoundException;

    GenreDto findByName(String name)
            throws GenreNotFoundException, IllegalArgumentException;

    List<GenreDto> findByNameLike(String name) throws IllegalArgumentException;

    List<GenreDto> findByBook(BookDto bookDto);

    List<GenreDto> findAll();

    GenreDto update(GenreDto genreDto)
            throws GenreNotFoundException, IllegalArgumentException;

    void delete(GenreDto genreDto)
            throws GenreNotFoundException, IllegalArgumentException;
}
