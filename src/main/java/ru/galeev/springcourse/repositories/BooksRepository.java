package ru.galeev.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.galeev.springcourse.models.Book;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByPersonId(int personId);

    @Transactional
    void deleteByPersonId(int personId);

    List<Book> findBooksByCreatedBetween(Calendar from, Calendar to);
}
