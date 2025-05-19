package org.abrohamovich.exceptions;

import jakarta.persistence.EntityExistsException;

public class BookAlreadyExistException extends EntityExistsException {
    public BookAlreadyExistException(String message) {
        super(message);
    }
}
