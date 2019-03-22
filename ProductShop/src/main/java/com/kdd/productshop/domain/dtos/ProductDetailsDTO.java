package com.kdd.productshop.domain.dtos;

import com.google.gson.annotations.Expose;

public class ProductDetailsDTO extends ProductDetailsBaseDTO {
    @Expose
    private String buyerFName;
    @Expose
    private String buyerLName;

    public ProductDetailsDTO() {
    }

    public String getBuyerFName() {
        return buyerFName;
    }

    public void setBuyerFName(String buyerFName) {
        this.buyerFName = buyerFName;
    }

    public String getBuyerLName() {
        return buyerLName;
    }

    public void setBuyerLName(String buyerLName) {
        this.buyerLName = buyerLName;
    }
}
