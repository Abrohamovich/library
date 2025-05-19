package org.abrohamovich.exceptions;

public class BookNotFoundException extends EntityException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
