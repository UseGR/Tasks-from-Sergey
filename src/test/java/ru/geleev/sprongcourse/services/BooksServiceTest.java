package ru.geleev.sprongcourse.services;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.galeev.springcourse.models.Book;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.repositories.BooksRepository;
import ru.galeev.springcourse.services.BooksService;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class BooksServiceTest {
    @Mock
    private BooksRepository booksRepository;

    private BooksService booksService;

    public BooksServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.booksService = new BooksService(booksRepository);
    }

    @Test
    public void findByPersonIdShouldReturnPersonsWithIdListOfBooks() {
        Book book1 = new Book(19, new Person(7, "Sauron", 1500, "dark_mage@mordor.com"), "Middle earth conquest", new GregorianCalendar(3049, Calendar.JANUARY, 1));
        Book book2 = new Book(20, new Person(7, "Sauron", 1500, "dark_mage@mordor.com"), "Rings of power", new GregorianCalendar(3033, Calendar.JANUARY, 1));
        Book book3 = new Book(17, new Person(7, "Sauron", 1500, "dark_mage@mordor.com"), "Dark sorcery", new GregorianCalendar(3025, Calendar.JANUARY, 1));
        Book book4 = new Book(18, new Person(7, "Sauron", 1500, "dark_mage@mordor.com"), "The history of high elves", new GregorianCalendar(3054, Calendar.JANUARY, 1));
        given(booksService.findByPersonId(7)).willReturn(Arrays.asList(book1, book2, book3, book4));

        List<Book> books = booksService.findByPersonId(7);
        assertThat(books).contains(book1);
        assertThat(books).contains(book2);
        assertThat(books).contains(book3);
        assertThat(books).contains(book4);
        assertEquals(4, books.size());

    }

    @Test
    public void findBooksByCreatedDateShouldReturnListOfBooksInRange() {
        Book book1 = new Book(19, new Person(7, "Sauron", 1500, "dark_mage@mordor.com"), "Middle earth conquest", new GregorianCalendar(3049, Calendar.JANUARY, 1));
        Book book2 = new Book(20, new Person(7, "Sauron", 1500, "dark_mage@mordor.com"), "Rings of power", new GregorianCalendar(3033, Calendar.JANUARY, 1));
        Book book3 = new Book(20, new Person(7, "Sauron", 1500, "dark_mage@mordor.com"), "Some book", new GregorianCalendar(3055, Calendar.JANUARY, 1));
        given(booksService.findBooksByCreatedDateBetween(new GregorianCalendar(3033, Calendar.JANUARY, 1), new GregorianCalendar(3049, Calendar.JANUARY, 1))).willReturn(Arrays.asList(book1, book2));

        List<Book> books = booksService.findBooksByCreatedDateBetween(new GregorianCalendar(3033, Calendar.JANUARY, 1), new GregorianCalendar(3049, Calendar.JANUARY, 1));
        assertThat(books).contains(book1);
        assertThat(books).contains(book2);
        assertThat(books).doesNotContain(book3);
        assertEquals(2, books.size());
    }

    @Test
    public void findBookByIdShouldReturnBookWithId() {
        given(booksService.findById(1)).willReturn(Optional.of(new Book(19, new Person(7, "Sauron", 1500, "dark_mage@mordor.com"), "Middle earth conquest", new GregorianCalendar(3049, Calendar.JANUARY, 1))));

        Book book = booksService.findById(1).orElse(null);
        assertEquals(19, book.getId());
    }

    @Test(expected = NullPointerException.class)
    public void findBookByIdShouldReturnNull() {
        Book book = booksService.findById(1).orElse(null);
        assertEquals(19, book.getId());
    }

    @Test
    public void saveShouldSaveBookById() {
        Book book = new Book(19, new Person(7, "Sauron", 1500, "dark_mage@mordor.com"), "Middle earth conquest", new GregorianCalendar(3049, Calendar.JANUARY, 1));
        booksService.save(book);
        verify(booksRepository).save(book);
    }

    @Test
    public void deleteByPersonIdShouldDeletePersonWithIdAllBooks() {
        booksService.deleteByPersonId(1);
        verify(booksRepository).deleteByPersonId(1);
    }

    @Test
    public void deleteByIdShouldDeleteBookWithId() {
        booksService.deleteById(1);
        verify(booksRepository).deleteById(1);
    }
}
