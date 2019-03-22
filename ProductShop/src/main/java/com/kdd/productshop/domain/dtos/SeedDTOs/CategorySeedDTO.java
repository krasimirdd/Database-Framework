package com.kdd.productshop.domain.dtos.SeedDTOs;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategorySeedDTO {
    @Expose
    private String name;

    public CategorySeedDTO() {
    }

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 symbols.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
