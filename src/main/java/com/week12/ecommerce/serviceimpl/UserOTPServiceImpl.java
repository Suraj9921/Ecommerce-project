package com.week12.ecommerce.serviceimpl;

import com.week12.ecommerce.model.UserOTP;
import com.week12.ecommerce.repository.UserOTPRepository;
import com.week12.ecommerce.service.UserOTPService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserOTPServiceImpl implements UserOTPService {
    private UserOTPRepository userOTPRepository;

    public UserOTPServiceImpl(UserOTPRepository userOTPRepository) {
        this.userOTPRepository = userOTPRepository;
    }

    @Override
    public void saveOrUpdate(UserOTP userOTP) {
        userOTPRepository.save(userOTP);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userOTPRepository.existsByEmail(email);
    }

    @Override
    public UserOTP findByEmail(String email) {
        return userOTPRepository.findByEmail(email);
    }
}
