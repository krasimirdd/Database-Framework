package com.kdd.productshop.repository;

import com.kdd.productshop.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT c FROM com.kdd.productshop.domain.entities.Category c")
    Set<Category> findAllCategories();


}
