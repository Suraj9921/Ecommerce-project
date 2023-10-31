package com.week12.ecommerce.model;

import com.week12.ecommerce.dto.NoWhitespace;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    @NoWhitespace
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    private double price;
    private double salePrice;
    private double weight;
    private String description;
    private String imageName;

    @Column(name = "quantity")
    private Integer qty;

    @Column(name = "is_activated")
    private boolean is_activated;
}
