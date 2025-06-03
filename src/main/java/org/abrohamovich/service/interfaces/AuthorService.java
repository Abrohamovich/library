package org.abrohamovich.service.interfaces;

import org.abrohamovich.dto.AuthorDto;
import org.abrohamovich.dto.BookDto;
import org.abrohamovich.entity.Sex;
import org.abrohamovich.exceptions.AuthorNotFoundException;
import org.abrohamovich.exceptions.EntityException;

import java.util.List;

public interface AuthorService {

    AuthorDto save(AuthorDto authorDto)
            throws EntityException, IllegalArgumentException;

    AuthorDto findById(long id)
            throws AuthorNotFoundException;

    List<AuthorDto> findBySex(Sex sex)
            throws IllegalArgumentException;

    List<AuthorDto> findByFullName(String fullName)
            throws IllegalArgumentException;

    List<AuthorDto> findByFullNameLike(String fullName)
            throws IllegalArgumentException;

    List<AuthorDto> findByBook(BookDto bookDto);

    List<AuthorDto> findAll();

    AuthorDto update(AuthorDto authorDto)
            throws AuthorNotFoundException, IllegalArgumentException;

    void delete(AuthorDto authorDto)
            throws AuthorNotFoundException, IllegalArgumentException;
}
