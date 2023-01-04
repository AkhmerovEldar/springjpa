package ru.slorimer.spring.services;

import org.hibernate.Hibernate;
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

    public List<Book> findAll(){
       return bookRepository.findAll();
    }

    public Book findOne(int id){
        Optional<Book> bookFind = bookRepository.findById(id);
        return bookFind.orElse(null);
    }

    @Transactional
    public void save(Book book){
        book.setTakenAt(new Date());
        bookRepository.save(book);
    }

    @Transactional
    public void update(Book updatedBook, int id){
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }
    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }

    public Optional<Person> getBookOwner(int id){
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()){
            Hibernate.initialize(book.get().getOwner());
            return Optional.ofNullable(book.get().getOwner());
        }
        else {
            return null;
        }
    }
    @Transactional
    public void assign(int id, Person person){
        bookRepository.findById(id).get().setOwner(person);
    }
    @Transactional
    public void release(int id){
        bookRepository.findById(id).get().setOwner(null);
    }
}
