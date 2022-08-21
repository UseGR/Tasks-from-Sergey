package ru.galeev.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.galeev.springcourse.models.Book;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByPersonId(int personId);

    @Transactional
    void deleteByPersonId(int personId);

    List<Book> findBooksByCreatedDateBetween(int from, int to);
}
