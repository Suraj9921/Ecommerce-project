package com.week12.ecommerce.service;


import com.week12.ecommerce.model.Product;
import com.week12.ecommerce.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart addItemToCart(Product product, int qty, String username);

    ShoppingCart updateTotalPrice(Double newTotalPrice,String username);

    ShoppingCart removeItemFromCart(Product product, String name);

    void clearCart(String username);
}
