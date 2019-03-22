package alararestaurant.repository;

import alararestaurant.domain.entities.Order;
import alararestaurant.domain.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    Optional<List<OrderItem>> findAllByOrder(Order order);
}
