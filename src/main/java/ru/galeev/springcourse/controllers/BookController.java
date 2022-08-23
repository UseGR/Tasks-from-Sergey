package ru.galeev.springcourse.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.galeev.springcourse.dto.BookDTO;
import ru.galeev.springcourse.models.Book;
import ru.galeev.springcourse.services.BooksService;
import ru.galeev.springcourse.services.PeopleService;
import ru.galeev.springcourse.util.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
public class BookController {
    private final PeopleService peopleService;
    private final BooksService bookService;
    private final ModelMapper modelMapper;

    @Autowired
    public BookController(PeopleService peopleService, BooksService bookService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/people/{personId}/books")
    public ResponseEntity<List<BookDTO>> getAllBooksByPersonId(@PathVariable(value = "personId") int personId) throws Exception {
        if (!peopleService.exists(personId)) {
            log.error("Person with id = {} isn't found", personId);
            throw new PersonNotFoundException("Person with id = " + personId + " isn't found!");
        }
        log.info("Person's with id = {} books:", personId);
        List<BookDTO> books = bookService.findByPersonId(personId).stream().map(this::convertToBookDTO).collect(Collectors.toList());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    @GetMapping("/books/{id}")
    public ResponseEntity<BookDTO> getBookByBookId(@PathVariable(value = "id") int id) {
        BookDTO bookDTO = convertToBookDTO(bookService.findById(id).orElseThrow(() -> new BookNotFoundException("Book with id = " + id + " isn't found")));
        log.info("Getting book by id = {}: {}", id, bookDTO);
        return new ResponseEntity<>(bookDTO, HttpStatus.OK);
    }

    @GetMapping("/books_range")
    public ResponseEntity<List<BookDTO>> getBooksInRange(@RequestParam int from, @RequestParam int to) {
        List<BookDTO> booksDTO = bookService.findBooksByCreatedDateBetween(from, to).stream().map(this::convertToBookDTO).collect(Collectors.toList());
        if (booksDTO.isEmpty()) {
            log.error("There is no books in range from {} to {} years", from, to);
            throw new BookNotFoundException("Books aren't found");
        }
        log.info("Books in range from {} to {} years: {}", from, to, booksDTO);
        return new ResponseEntity<>(booksDTO, HttpStatus.OK);
    }

    @PostMapping("/people/{personId}/books")
    public ResponseEntity<BookDTO> createBook(@PathVariable(value = "personId") int personId,
                                              @RequestBody BookDTO bookRequest) {
        BookDTO comment = convertToBookDTO(peopleService.findOne(personId).map(personDTO -> {
            bookRequest.setPerson(personDTO);
            return bookService.save(convertToBook(bookRequest));
        }).orElseThrow(() -> new BookNotCreatedException("Book isn't created!")));
        log.info("Book {} created with personId = {}", bookRequest, personId);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable("id") int id, @RequestBody BookDTO bookRequest) {
        BookDTO book = convertToBookDTO(bookService.findById(id).orElseThrow(() -> new BookNotFoundException("Book with id = " + id + " isn't found")));
        book.setName(bookRequest.getName());
        book.setCreated(book.getCreated());
        log.info("Book with id = {} was updated, new data: {}", id, bookRequest);
        return new ResponseEntity<>(convertToBookDTO(bookService.save(convertToBook(book))), HttpStatus.OK);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") int id) {
        getBookByBookId(id);
        bookService.deleteById(id);
        log.info("Book with id = {} was removed", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/people/{personId}/books")
    public ResponseEntity<List<Book>> deleteAllBooksOfPerson(@PathVariable(value = "personId") int personId) throws Exception {
        if (!peopleService.exists(personId)) {
            log.error("Person with id = {} wasn't found!", personId);
            throw new PersonNotFoundException("Person with id = " + personId + " wasn't found!");
        }
        bookService.deleteByPersonId(personId);
        log.info("Person with id = {} hasn't got books", personId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> globalExceptionHandler(RuntimeException e) {
        PersonErrorResponse response = new PersonErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Book convertToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }
}
