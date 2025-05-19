package org.abrohamovich.service.interfaces;

import org.abrohamovich.dto.BookDto;
import org.abrohamovich.dto.BookInstanceDto;
import org.abrohamovich.dto.PublisherDto;
import org.abrohamovich.entity.Format;
import org.abrohamovich.entity.Status;
import org.abrohamovich.exceptions.BookInstanceNotFoundException;
import org.abrohamovich.exceptions.EntityException;

import java.util.List;

public interface BookInstanceService {

    BookInstanceDto save(BookInstanceDto bookInstanceDto)
            throws EntityException, IllegalArgumentException;

    BookInstanceDto findById(long id)
            throws BookInstanceNotFoundException;

    List<BookInstanceDto> findByBook(BookDto bookDto)
            throws IllegalArgumentException;

    List<BookInstanceDto> findByPublisher(PublisherDto publisherDto)
            throws IllegalArgumentException;

    List<BookInstanceDto> findByStatus(Status status)
            throws IllegalArgumentException;

    List<BookInstanceDto> findByFormat(Format format)
            throws IllegalArgumentException;

    List<BookInstanceDto> findAll();

    BookInstanceDto update(BookInstanceDto bookInstanceDto)
            throws BookInstanceNotFoundException, IllegalArgumentException;

    void delete(BookInstanceDto bookInstanceDto)
            throws BookInstanceNotFoundException, IllegalArgumentException;
}
