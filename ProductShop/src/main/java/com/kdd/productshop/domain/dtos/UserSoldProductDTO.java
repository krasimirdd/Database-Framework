package com.kdd.productshop.domain.dtos;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UserSoldProductDTO {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private List<ProductDetailsDTO> products;

    public UserSoldProductDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<ProductDetailsDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetailsDTO> products) {
        this.products = products;
    }
}
