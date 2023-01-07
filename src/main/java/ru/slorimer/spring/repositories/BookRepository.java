package ru.slorimer.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slorimer.spring.models.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{
    List<Book> findByBookNameStartingWith(String bookName);
}
