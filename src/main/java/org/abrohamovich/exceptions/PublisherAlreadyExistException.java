package org.abrohamovich.exceptions;

import jakarta.persistence.EntityExistsException;

public class PublisherAlreadyExistException extends EntityExistsException {
    public PublisherAlreadyExistException(String message) {
        super(message);
    }
}
