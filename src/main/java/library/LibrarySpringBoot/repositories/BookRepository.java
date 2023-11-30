package library.LibrarySpringBoot.repositories;

import library.LibrarySpringBoot.models.Book;
import library.LibrarySpringBoot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person person);
    List<Book> findAllByTitleStartsWith(String request);

}
