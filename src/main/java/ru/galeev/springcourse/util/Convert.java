package ru.galeev.springcourse.util;

import org.modelmapper.ModelMapper;
import ru.galeev.springcourse.dto.BookDTO;
import ru.galeev.springcourse.dto.PersonDTO;
import ru.galeev.springcourse.models.Book;
import ru.galeev.springcourse.models.Person;

public class Convert {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    public static PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    public static BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    public static Book convertToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }
}
