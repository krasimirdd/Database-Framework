package com.kdd.productshop.repository;

import com.kdd.productshop.domain.entities.Product;
import com.kdd.productshop.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByPriceBetweenAndBuyerOrderByPrice(BigDecimal more, BigDecimal less, User buyer);

    List<Product> findAllBySeller_IdAndBuyerIsNotNull(Integer id);

    @Query(value = "SELECT count(p.id) FROM product_shop_db.products p WHERE p.buyer_id is not null AND p.seller_id = ?", nativeQuery = true)
    Integer countProductByBuyerIsNotNull(@Param(value = "id") Integer id);


}
