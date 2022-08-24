package ru.galeev.springcourse.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.galeev.springcourse.dto.BookDTO;
import ru.galeev.springcourse.models.Book;
import ru.galeev.springcourse.services.BooksService;
import ru.galeev.springcourse.services.PeopleService;
import ru.galeev.springcourse.util.*;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
public class BookController {
    private final PeopleService peopleService;
    private final BooksService bookService;

    @Autowired
    public BookController(PeopleService peopleService, BooksService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping("/people/{personId}/books")
    public ResponseEntity<List<BookDTO>> getAllBooksByPersonId(@PathVariable(value = "personId") int personId) throws Exception {
        if (!peopleService.exists(personId)) {
            log.error("Person with id = {} wasn't found", personId);
            throw new PersonNotFoundException("Person with id = " + personId + " wasn't found!");
        }
        log.info("Person's with id = {} books:", personId);
        List<BookDTO> books = bookService.findByPersonId(personId).stream().map(Convert::convertToBookDTO).collect(Collectors.toList());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    @GetMapping("/books/{id}")
    public ResponseEntity<BookDTO> getBookByBookId(@PathVariable(value = "id") int id) {
        BookDTO bookDTO = Convert.convertToBookDTO(bookService.findById(id).orElseThrow(() -> new BookNotFoundException("Book with id = " + id + " wasn't found!")));
        log.info("Getting book by id = {}: {}", id, bookDTO);
        return new ResponseEntity<>(bookDTO, HttpStatus.OK);
    }

    @GetMapping("/books_range")
    public ResponseEntity<List<BookDTO>> getBooksInRange(@RequestParam @DateTimeFormat(pattern = "yyyy") Calendar from, @RequestParam @DateTimeFormat(pattern = "yyyy") Calendar to) {
        List<BookDTO> booksDTO = bookService.findBooksByCreatedDateBetween(from, to).stream().map(Convert::convertToBookDTO).collect(Collectors.toList());
        if (booksDTO.isEmpty()) {
            log.error("There is no books in range from {} to {} years", from, to);
            throw new BookNotFoundException("Books wasn't found");
        }
        log.info("Books in range from {} to {} years: {}", from.get(Calendar.YEAR), to.get(Calendar.YEAR), booksDTO);
        return new ResponseEntity<>(booksDTO, HttpStatus.OK);
    }

    @PostMapping("/people/{personId}/books")
    public ResponseEntity<BookDTO> createBook(@PathVariable(value = "personId") int personId,
                                              @RequestBody BookDTO bookRequest) {
        BookDTO comment = Convert.convertToBookDTO(peopleService.findOne(personId).map(personDTO -> {
            bookRequest.setPerson(personDTO);
            return bookService.save(Convert.convertToBook(bookRequest));
        }).orElseThrow(() -> new BookNotCreatedException("Book wasn't created!")));
        log.info("Book {} created with personId = {}", bookRequest, personId);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable("id") int id, @RequestBody BookDTO bookRequest) {
        BookDTO book = Convert.convertToBookDTO(bookService.findById(id).orElseThrow(() -> new BookNotFoundException("Book with id = " + id + " wasn't found")));
        book.setName(bookRequest.getName());
        book.setCreated(book.getCreated());
        log.info("Book with id = {} was updated, new data: {}", id, bookRequest);
        return new ResponseEntity<>(Convert.convertToBookDTO(bookService.save(Convert.convertToBook(book))), HttpStatus.OK);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") int id) {
        getBookByBookId(id);
        bookService.deleteById(id);
        log.info("Book with id = {} was removed", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/people/{personId}/books")
    public ResponseEntity<List<Book>> deleteAllBooksOfPerson(@PathVariable(value = "personId") int personId) {
        if (!peopleService.exists(personId)) {
            log.error("Person with id = {} wasn't found!", personId);
            throw new PersonNotFoundException("Person with id = " + personId + " wasn't found!");
        }
        bookService.deleteByPersonId(personId);
        log.info("Person with id = {} hasn't got books", personId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
