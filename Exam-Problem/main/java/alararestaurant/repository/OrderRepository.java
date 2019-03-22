package alararestaurant.repository;

import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByEmployee(Employee employeeEntity);

    @Query(value =
            "select * from orders o join employees e on o.employee_id = e.id join positions p on e.position_id = p.id WHERE p.name = \"Burger Flipper\" order by e.name, o.id", nativeQuery = true)
    List<Order> exportOrders();
}
