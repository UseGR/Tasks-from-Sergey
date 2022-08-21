package ru.galeev.springcourse.util;

public class PersonNotFoundException extends RuntimeException{
    public PersonNotFoundException() {

    }
    public PersonNotFoundException(String message) {
        super(message);
    }


}
