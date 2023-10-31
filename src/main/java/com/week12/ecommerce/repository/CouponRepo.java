package com.week12.ecommerce.repository;


import com.week12.ecommerce.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepo extends JpaRepository<Coupon,Integer> {
    Optional<Coupon> findCouponByName(String name);
    boolean existsCouponByName(String name);

    Coupon findCouponByCode(String couponCode);
}
