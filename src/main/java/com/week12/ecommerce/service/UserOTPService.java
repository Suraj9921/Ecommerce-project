package com.week12.ecommerce.service;


import com.week12.ecommerce.model.UserOTP;

public interface UserOTPService {
    void saveOrUpdate(UserOTP userOTP);

    boolean existsByEmail(String email);

    UserOTP findByEmail(String email);
}
