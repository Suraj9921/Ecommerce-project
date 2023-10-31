package com.week12.ecommerce.repository;

import com.week12.ecommerce.model.UserOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOTPRepository extends JpaRepository<UserOTP,Long> {
    boolean existsByEmail(String email);

    UserOTP findByEmail(String email);
}
