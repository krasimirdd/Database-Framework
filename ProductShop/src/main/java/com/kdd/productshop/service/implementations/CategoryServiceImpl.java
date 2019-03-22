package com.kdd.productshop.service.implementations;

import com.kdd.productshop.domain.dtos.CategoryByProductsDTO;
import com.kdd.productshop.domain.dtos.SeedDTOs.CategorySeedDTO;
import com.kdd.productshop.domain.entities.Category;
import com.kdd.productshop.domain.entities.Product;
import com.kdd.productshop.repository.CategoryRepository;
import com.kdd.productshop.service.CategoryService;
import com.kdd.productshop.util.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ValidatorUtil validatorUtil, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedCategories(CategorySeedDTO[] categorySeedDTOS) {
        for (CategorySeedDTO categorySeedDTO : categorySeedDTOS) {
            if (!this.validatorUtil.isValid(categorySeedDTO)) {
                this.validatorUtil.violations(categorySeedDTO)
                        .forEach(v -> System.out.println(v.getMessage()));

                continue;
            }

            Category entity = this.modelMapper.map(categorySeedDTO, Category.class);
            this.categoryRepository.saveAndFlush(entity);
        }
    }

    @Override
    public List<CategoryByProductsDTO> getAllCategories() {
        Set<Category> categoryEntities = this.categoryRepository.findAllCategories();

        List<CategoryByProductsDTO> categoryByProductsDTOS = new ArrayList<>();

        for (Category categoryEntity : categoryEntities) {
            CategoryByProductsDTO categoryByProductsDTO =
                    modelMapper.map(categoryEntity, CategoryByProductsDTO.class);


            List<Product> products = categoryEntity.getProducts();
            BigDecimal productsCount = BigDecimal.valueOf(products.size());
            categoryByProductsDTO.setProductsCount(productsCount);

            BigDecimal sum = BigDecimal.valueOf(0.0);
            for (Product product : products) {
                sum = sum.add(product.getPrice());
            }
            if (products.size() != 0) {
                BigDecimal avgSum = sum.divide(productsCount, RoundingMode.HALF_UP);

                categoryByProductsDTO.setAveragePrice(avgSum);
                categoryByProductsDTO.setTotalRevenue(avgSum.multiply(productsCount));
            } else {
                categoryByProductsDTO.setAveragePrice(BigDecimal.valueOf(0.0));
                categoryByProductsDTO.setTotalRevenue(BigDecimal.valueOf(0.0));
            }
            categoryByProductsDTOS.add(categoryByProductsDTO);

        }
        return categoryByProductsDTOS;
    }
}
