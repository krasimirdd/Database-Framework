package com.kdd.productshop.domain.dtos;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class ProductDTO {
    @Expose
    private Integer count;
    @Expose
    private Set<ProductDetailsBaseDTO> productDetailsDTOs;

    public ProductDTO() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Set<ProductDetailsBaseDTO> getProductDetailsDTOs() {
        return productDetailsDTOs;
    }

    public void setProductDetailsDTOs(Set<ProductDetailsBaseDTO> productDetailsDTOs) {
        this.productDetailsDTOs = productDetailsDTOs;
    }

    public void addDetails(ProductDetailsBaseDTO productDetailsBaseDTO) {
        this.productDetailsDTOs.add(productDetailsBaseDTO);
    }
}
