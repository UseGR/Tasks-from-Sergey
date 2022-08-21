package ru.galeev.springcourse.util;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
