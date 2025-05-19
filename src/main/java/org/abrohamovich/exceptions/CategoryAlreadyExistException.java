package org.abrohamovich.exceptions;

import jakarta.persistence.EntityExistsException;

public class CategoryAlreadyExistException extends EntityExistsException {
    public CategoryAlreadyExistException(String message) {
        super(message);
    }
}
