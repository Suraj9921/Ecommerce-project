package com.week12.ecommerce.service;

import com.week12.ecommerce.model.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersSevice {
    public boolean findAlreadyExistUserByEmail(String email);

    Users findByEmail(String email);

    void saveOrUpdate(Users newUser);

    boolean existsByEmail(String email);

    String genrateOTPAndSendOnEmail(String email);

    List<Users> findAll();

    boolean verifyOTP(long otp, String username);

    boolean existById(long id);

    Users findById(long id);

    public Users findUserByEmail(String email);

    Users findUserByUsername(String name);
}
