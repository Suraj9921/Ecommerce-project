package com.week12.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "shopping_cart")
@Data
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopping_cart_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private Users user;

    private double totalPrice;
    private int totalItems;
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL)
    private Set<CartItem> cartItems;

    @Override
    public int hashCode() {
//        return Objects.hash(id, user, totalPrice, totalItems); // Exclude cartItems
        return Objects.hash(id,user,totalPrice,totalItems);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingCart that)) return false;
        return id.equals(that.id);
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", user=" + user +
                ", totalPrice=" + totalPrice +
                ", totalItems=" + totalItems +
                ", cartItems=" + cartItems +
                '}';
    }
}
