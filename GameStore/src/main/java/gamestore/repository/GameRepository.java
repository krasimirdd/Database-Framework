package gamestore.repository;

import gamestore.domain.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, String> {
    Optional<Game> findByTitle(String title);

    Optional<Game> findById(long id);

    void deleteById(int id);

    @Query(value = "SELECT g FROM gamestore.domain.entities.Game AS g")
    Optional<List<Game>> getAllGames();

    Optional<Game> getByTitle(String title);

    @Query(value = "SELECT g FROM gamestore.domain.entities.Game g JOIN gamestore.domain.entities.User u where u.fullName = :user")
    List<Game> findOwnedGames(@Param(value = "user") String user);
}
