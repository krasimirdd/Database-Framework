package com.kdd.productshop.service;

import com.kdd.productshop.domain.dtos.ProductInRangeDTO;
import com.kdd.productshop.domain.dtos.SeedDTOs.ProductSeedDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    void seedProducts(ProductSeedDTO[] productSeedDTOS);

    List<ProductInRangeDTO> productsInRange(BigDecimal more, BigDecimal less);

}
