package com.kdd.productshop.domain.dtos;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class CategoryByProductsDTO {
    @Expose
    private String name;
    @Expose
    private BigDecimal productsCount;
    @Expose
    private BigDecimal averagePrice;
    @Expose
    private BigDecimal totalRevenue;

    public CategoryByProductsDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(BigDecimal productsCount) {
        this.productsCount = productsCount;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
