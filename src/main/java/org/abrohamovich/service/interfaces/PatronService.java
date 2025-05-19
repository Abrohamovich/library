package org.abrohamovich.service.interfaces;

import org.abrohamovich.dto.BookInstanceDto;
import org.abrohamovich.dto.PatronDto;
import org.abrohamovich.exceptions.EntityException;
import org.abrohamovich.exceptions.PatronAlreadyExistException;
import org.abrohamovich.exceptions.PatronNotFoundException;

import java.util.List;

public interface PatronService {

    PatronDto save(PatronDto patronDto)
            throws PatronAlreadyExistException, EntityException, IllegalArgumentException;

    PatronDto findById(long id)
            throws PatronNotFoundException;

    PatronDto findByCardId(String cardId)
            throws PatronNotFoundException, IllegalArgumentException;

    PatronDto findByBookInstance(BookInstanceDto bookInstanceDto)
            throws PatronNotFoundException, IllegalArgumentException;

    List<PatronDto> findByFullName(String fullName)
            throws IllegalArgumentException;

    PatronDto findByEmail(String email)
            throws PatronNotFoundException, IllegalArgumentException;

    PatronDto findByPhone(String phone)
            throws PatronNotFoundException, IllegalArgumentException;

    PatronDto findByAddress(String address)
            throws PatronNotFoundException, IllegalArgumentException;

    List<PatronDto> findAll();

    PatronDto update(PatronDto patronDto)
            throws PatronNotFoundException, IllegalArgumentException;

    void delete(PatronDto patronDto)
            throws PatronNotFoundException, IllegalArgumentException;
}
