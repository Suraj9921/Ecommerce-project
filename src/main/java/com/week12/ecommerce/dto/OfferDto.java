package com.week12.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDto {

    private Long id;

    private String name;

    private String description;


    private int offPercentage;


    private String offerType;


    private Long offerProductId;

    private String applicableForProductName;

    private Long offerCategoryId;

    private String applicableForCategoryName;

    private boolean enabled;


}
