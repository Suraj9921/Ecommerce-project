package com.week12.ecommerce.repository;

import com.week12.ecommerce.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Long> {

    List<Wishlist> findAllByUsersId(long id);

    @Query(value = "SELECT EXISTS (SELECT FROM wishlist WHERE customer_id = :customerId AND product_id=:productId)",nativeQuery = true)
    boolean findByProductIdAndCustomerId(@Param("productId") long productId,@Param("customerId") long customerId);

    Wishlist findById(long id);

}
