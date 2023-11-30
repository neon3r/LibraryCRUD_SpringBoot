package library.LibrarySpringBoot.services;

import library.LibrarySpringBoot.models.Book;
import library.LibrarySpringBoot.models.Person;
import library.LibrarySpringBoot.repositories.BookRepository;
import library.LibrarySpringBoot.repositories.PeopleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BookRepository bookRepository;

    public PeopleService(PeopleRepository peopleRepository, BookRepository bookRepository) {
        this.peopleRepository = peopleRepository;
        this.bookRepository = bookRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll(Sort.by("id"));
    }

    public List<Person> findAll(int page, int itemsPerPage, String sortBy) {
        return peopleRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by(sortBy))).getContent();
    }

    public Person findOne(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void create(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> bookList(int id) {
        return bookRepository.findByOwner(peopleRepository.findById(id).orElse(null));
    }

    public List<Person> findAllByNameStartsWith(String request) {
        return peopleRepository.findAllByNameStartsWith(request);
    }
}
