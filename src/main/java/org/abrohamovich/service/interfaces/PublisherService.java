package org.abrohamovich.service.interfaces;

import org.abrohamovich.dto.BookDto;
import org.abrohamovich.dto.PublisherDto;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.PublisherAlreadyExistException;
import org.abrohamovich.exceptions.PublisherNotFoundException;

import java.util.List;

public interface PublisherService {

    PublisherDto save(PublisherDto publisherDto)
            throws PublisherAlreadyExistException, EntityException, IllegalArgumentException;

    PublisherDto findById(long id)
            throws PublisherNotFoundException;

    PublisherDto findByName(String name)
            throws PublisherNotFoundException, IllegalArgumentException;

    List<PublisherDto> findByNameLike(String name) throws IllegalArgumentException;

    PublisherDto findByEmail(String email)
            throws PublisherNotFoundException, IllegalArgumentException;

    PublisherDto findByWebsite(String website)
            throws PublisherNotFoundException, IllegalArgumentException;

    PublisherDto findByAddress(String address)
            throws PublisherNotFoundException, IllegalArgumentException;

    PublisherDto findByBook(BookDto bookDto);

    List<PublisherDto> findAll();

    PublisherDto update(PublisherDto publisherDto)
            throws PublisherNotFoundException, IllegalArgumentException;

    void delete(PublisherDto publisherDto)
            throws PublisherNotFoundException, IllegalArgumentException;
}
