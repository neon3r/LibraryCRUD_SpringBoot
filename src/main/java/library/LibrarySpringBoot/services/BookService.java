package library.LibrarySpringBoot.services;

import library.LibrarySpringBoot.models.Book;
import library.LibrarySpringBoot.repositories.BookRepository;
import library.LibrarySpringBoot.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PeopleRepository peopleRepository) {
        this.bookRepository = bookRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll(Sort.by("id"));
    }

    public List<Book> findAll(int page, int itemsPerPage, String sortBy) {
        return bookRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by(sortBy))).getContent();
    }

    public Book findOne(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void create(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void release(int id) {
        bookRepository.findById(id).orElse(null).setOwner(null);
    }

    @Transactional
    public void take(int id, Book book) {
        bookRepository.findById(id).orElse(null).setOwner(peopleRepository.findById(book.getOwnerId()).orElse(null));
    }

    public List<Book> findAllByTitleStartsWith(String request){
        return bookRepository.findAllByTitleStartsWith(request);
    }

}
