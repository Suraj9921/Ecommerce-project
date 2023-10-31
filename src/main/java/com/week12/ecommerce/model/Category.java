package com.week12.ecommerce.model;

import com.week12.ecommerce.dto.NoWhitespace;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name = "category")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private int id;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @NoWhitespace
    private String name;

    @Column(name = "is_activated")
    private boolean activated;

}
