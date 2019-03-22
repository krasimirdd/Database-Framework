package bookshopsystemapp.repository;

import bookshopsystemapp.domain.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query(value = "select a from bookshopsystemapp.domain.entities.Author as a where a.firstName like :endsWith")
    List<Author> firstNameByPattern(@Param(value = "endsWith") String wildcard);

    @Query(value = "SELECT concat(a.firstName,' ',a.lastName,' - ', sum(b.copies)) from bookshopsystemapp.domain.entities.Author a join bookshopsystemapp.domain.entities.Book b on b.author=a group by a.id ORDER BY sum(b.copies) desc")
    List<String> getAllWithTheirBookCopies();
}
