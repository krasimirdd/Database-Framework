/*
package com.kdd.productshop.web.controllers;

import com.google.gson.Gson;
import com.kdd.productshop.domain.dtos.ProductInRangeDTO;
import com.kdd.productshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class ProductShopRestController {

    private final ProductService productService;
    private final Gson gson;

    @Autowired
    public ProductShopRestController(ProductService productService, Gson gson) {
        this.productService = productService;
        this.gson = gson;
    }


    @GetMapping("/")
    private String productsInRange() throws IOException {
        List<ProductInRangeDTO> productInRangeDTOS = this.productService
                .productsInRange(BigDecimal.valueOf(500), BigDecimal.valueOf(1000));

        String productsInRangeJSON = this.gson.toJson(productInRangeDTOS).replace("\n", "<br>");

        return productsInRangeJSON;
    }
}*/
