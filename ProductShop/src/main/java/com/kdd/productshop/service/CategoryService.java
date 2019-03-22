package com.kdd.productshop.service;

import com.kdd.productshop.domain.dtos.CategoryByProductsDTO;
import com.kdd.productshop.domain.dtos.SeedDTOs.CategorySeedDTO;

import java.util.List;

public interface CategoryService {
    void seedCategories(CategorySeedDTO[] categorySeedDTOS);

    List<CategoryByProductsDTO> getAllCategories();
}
