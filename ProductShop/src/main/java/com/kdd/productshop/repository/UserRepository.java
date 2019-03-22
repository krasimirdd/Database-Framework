package com.kdd.productshop.repository;

import com.kdd.productshop.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM product_shop_db.users u JOIN product_shop_db.products p ON u.id = p.seller_id HAVING p.buyer_id IS NOT NULL",nativeQuery = true)
    Set<User> getUserHavingItemsSold();

}
