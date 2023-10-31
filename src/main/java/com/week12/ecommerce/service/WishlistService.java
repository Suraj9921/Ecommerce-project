package com.week12.ecommerce.service;


import com.week12.ecommerce.model.Users;
import com.week12.ecommerce.model.Wishlist;

import java.util.List;

public interface WishlistService {

    List<Wishlist> findAllByCustomer(Users customer);

    boolean findByProductId(long id,Users customer);

    Wishlist save(long productId,Users customer);

    void deleteWishlist(long id);
}
