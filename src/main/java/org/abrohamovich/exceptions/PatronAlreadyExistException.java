package org.abrohamovich.exceptions;

import jakarta.persistence.EntityExistsException;

public class PatronAlreadyExistException extends EntityExistsException {
    public PatronAlreadyExistException(String message) {
        super(message);
    }
}
