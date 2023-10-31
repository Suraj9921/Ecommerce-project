package com.week12.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int id;

    @NotBlank(message = "Should not be blank")
    private String house_no;
    @NotBlank(message = "Should not be blank")
    private String street;
    @NotBlank(message = "Should not be blank")
    private String state;
    @NotBlank(message = "Should not be blank")
    private String postalcode;
    @NotBlank(message = "Should not be blank")
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private Users user;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", state='" + state + '\'' +
                ", postalcode='" + postalcode + '\'' +
                ", country='" + country + '\'' +
                ", user=" + user +
                '}';
    }
}
