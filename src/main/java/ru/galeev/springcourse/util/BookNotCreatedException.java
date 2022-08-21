package ru.galeev.springcourse.util;

public class BookNotCreatedException extends RuntimeException{
    public BookNotCreatedException(String message) {
        super(message);
    }
}
