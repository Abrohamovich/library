package org.abrohamovich.exceptions;

public class AuthorNotFoundException extends EntityException {
    public AuthorNotFoundException(String message) {
        super(message);
    }
}
