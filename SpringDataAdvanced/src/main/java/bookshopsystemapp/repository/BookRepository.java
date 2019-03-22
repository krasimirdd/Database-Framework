package bookshopsystemapp.repository;

import bookshopsystemapp.domain.entities.AgeRestriction;
import bookshopsystemapp.domain.entities.Book;
import bookshopsystemapp.domain.entities.EditionType;
import bookshopsystemapp.domain.entities.ReducedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {


    List<Book> findAllByReleaseDateAfter(LocalDate date);

    List<Book> findAllByReleaseDateBefore(LocalDate date);

    List<Book> findAllByAgeRestriction(AgeRestriction restriction);

    List<Book> findAllByEditionType(EditionType editionType);

    @Query(value = "SELECT b FROM bookshopsystemapp.domain.entities.Book AS b where b.price < 5.0 OR b.price > 40.0")
    List<Book> findAllByPrice();

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate before, LocalDate after);

    List<Book> findAllByTitleContains(String pattern);

    @Query(value =
            "SELECT b FROM bookshopsystemapp.domain.entities.Book AS b" +
                    " where b.author.lastName like :wildcard")
    List<Book> findAllByAuthorLastName(@Param(value = "wildcard") String pattern);

    @Query(value = "SELECT count(b.id) FROM bookshopsystemapp.domain.entities.Book b WHERE LENGTH(b.title) >= :length")
    Integer countBooksByTitleLength(@Param(value = "length") Integer num);

    ReducedBook findByTitle(String title);
}
