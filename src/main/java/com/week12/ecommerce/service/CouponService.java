package com.week12.ecommerce.service;

import com.week12.ecommerce.model.Coupon;
import com.week12.ecommerce.repository.CouponRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CouponService {
    @Autowired
    CouponRepo repo;

    public List<Coupon> getAllCoupons() {
        return repo.findAll();
    }

    public void addCoupon(Coupon coupon) {
        repo.save(coupon);
    }

    public void updateCoupon(Coupon coupon) {
        repo.save(coupon);
    }

    public Optional<Coupon> getCouponById(int id) {
        return repo.findById(id);
    }

    public double calculateDiscount(String couponCode, double totalPrice) {
        Coupon coupon = findCouponByCode(couponCode);
        System.out.println(" "+coupon);
        if (coupon != null && coupon.isActive() && totalPrice > coupon.getMinimum()) {
            return coupon.getDiscount();
        }
        return -1;
//        if (coupon != null && coupon.isActive()) {
//            double discountPercentage = coupon.getDiscount() / 100.0;
//
//            double totalPrice = cartService.getTotalPrice();
//
//            double discountAmount = totalPrice * discountPercentage;
//
//            return discountAmount;
//        }
//        return -1;
    }

    private Coupon findCouponByCode(String couponCode) {
        return repo.findCouponByCode(couponCode);
    }
}
