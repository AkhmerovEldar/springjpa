package ru.slorimer.spring.services;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slorimer.spring.models.Book;
import ru.slorimer.spring.models.Person;
import ru.slorimer.spring.repositories.PeopleRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;


    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }
    public Person findOne(int id){
        Optional<Person> findPerson = peopleRepository.findById(id);
        return findPerson.orElse(null);
    }
    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }
    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }
    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }

    public List<Book> getBooksByPersonId(int id){
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()){
            Hibernate.initialize(person.get().getBooks());

//            person.get().getBooks().forEach(book -> {
//                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
//                if (diffInMillies > 864000000)
//                    book.setExpired(true);
//            });
            return person.get().getBooks();
        }
        else {
            return Collections.emptyList();
        }
    }
}
