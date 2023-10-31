package com.week12.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDto {

    @NotNull
    private Long id;

    @NotBlank
    @NotEmpty
    private String name;

    private int categoryId;

    private double price;

    private double salePrice;

    private double weight;

    private String description;

    private String imageName;

    private int qty;

    private boolean activated;
}
