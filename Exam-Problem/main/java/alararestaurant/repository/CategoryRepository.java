package alararestaurant.repository;

import alararestaurant.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

    @Query(value = "select * from categories c join items i on c.id = i.category_id group by i.category_id order by count(i.id) desc, sum(i.price) desc", nativeQuery = true)
    List<Category> exportByCount();
}
