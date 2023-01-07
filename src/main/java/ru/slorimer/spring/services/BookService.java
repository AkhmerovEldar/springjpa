package ru.slorimer.spring.services;

import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slorimer.spring.models.Book;
import ru.slorimer.spring.models.Person;
import ru.slorimer.spring.repositories.BookRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class BookService {

    public final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(boolean sort_by_year){
        if(sort_by_year)
            return bookRepository.findAll(Sort.by("createdYear"));
        else
            return bookRepository.findAll();
    }

    public List<Book> findWithPagination(Integer page, Integer books_by_page, boolean sort_by_year){
        if(sort_by_year)
            return bookRepository.findAll(PageRequest.of(page, books_by_page, Sort.by("createdYear"))).getContent();
        else
            return bookRepository.findAll(PageRequest.of(page, books_by_page)).getContent();
    }

    public List<Book> searchBook(String bookName){
        return bookRepository.findByBookNameStartingWith(bookName);
    }

    public Book findOne(int id){
        Optional<Book> bookFind = bookRepository.findById(id);
        return bookFind.orElse(null);
    }

    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }

    @Transactional
    public void update(Book updatedBook, int id){
        Book bookToBeUpdated = bookRepository.findById(id).get();
        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        bookRepository.save(updatedBook);
    }
    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }

    public Person getBookOwner(int id){
        return bookRepository.findById(id).map(Book::getOwner).orElse(null);
    }
    @Transactional
    public void assign(int id, Person person){
        bookRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(person);
                    book.setTakenAt(new Date());
                }
        );
    }
    @Transactional
    public void release(int id){
        bookRepository.findById(id).ifPresent(
                book -> {
                    book.setTakenAt(null);
                    book.setOwner(null);
                }
        );
    }
}
