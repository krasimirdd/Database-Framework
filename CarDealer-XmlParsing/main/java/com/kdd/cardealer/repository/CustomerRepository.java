package com.kdd.cardealer.repository;

import com.kdd.cardealer.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value =
            "SELECT c from com.kdd.cardealer.domain.entities.Customer c ORDER BY birthDate asc")
    List<Customer> getAllCustomersOrderedByBirthDate();
}
