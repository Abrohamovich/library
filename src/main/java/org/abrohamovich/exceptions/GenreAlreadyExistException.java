package org.abrohamovich.exceptions;

import jakarta.persistence.EntityExistsException;

public class GenreAlreadyExistException extends EntityExistsException {
    public GenreAlreadyExistException(String message) {
        super(message);
    }
}
