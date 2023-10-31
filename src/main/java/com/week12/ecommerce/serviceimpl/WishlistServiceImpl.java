package com.week12.ecommerce.serviceimpl;

import com.week12.ecommerce.model.Product;
import com.week12.ecommerce.model.Users;
import com.week12.ecommerce.model.Wishlist;
import com.week12.ecommerce.repository.WishlistRepository;
import com.week12.ecommerce.service.ProductService;
import com.week12.ecommerce.service.WishlistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {


    private WishlistRepository wishlistRepository;

    private ProductService productService;

    public WishlistServiceImpl(WishlistRepository wishlistRepository, ProductService productService) {
        this.productService=productService;
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public List<Wishlist> findAllByCustomer(Users customer) {
        List<Wishlist> wishlists = wishlistRepository.findAllByUsersId(customer.getId());
        return wishlists;
    }

    @Override
    public boolean findByProductId(long productId,Users customer) {

        boolean exists= wishlistRepository.findByProductIdAndCustomerId(productId,customer.getId());


        return exists;
    }

    @Override
    public Wishlist save(long productId, Users customer) {

        Product product=productService.findBYId(productId);
        Wishlist wishlist=new Wishlist();
        wishlist.setProduct(product);
        wishlist.setUsers(customer);
        wishlistRepository.save(wishlist);
        return wishlist;
    }

    @Override
    public void deleteWishlist(long id) {
        Wishlist wishlist=wishlistRepository.findById(id);
        wishlistRepository.delete(wishlist);
    }
}
