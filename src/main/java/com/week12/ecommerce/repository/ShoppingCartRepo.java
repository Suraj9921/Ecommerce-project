package com.week12.ecommerce.repository;


import com.week12.ecommerce.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepo extends JpaRepository<ShoppingCart, Long> {

}
