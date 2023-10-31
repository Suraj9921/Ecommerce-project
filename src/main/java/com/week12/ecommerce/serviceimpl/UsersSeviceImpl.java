package com.week12.ecommerce.serviceimpl;

import com.week12.ecommerce.model.EmailDetails;
import com.week12.ecommerce.model.Users;
import com.week12.ecommerce.repository.UsersRepository;
import com.week12.ecommerce.service.EmailService;
import com.week12.ecommerce.service.UsersSevice;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsersSeviceImpl implements UsersSevice {

    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;

    private EmailService emailService;

    public UsersSeviceImpl(UsersRepository usersRepository,
                           PasswordEncoder passwordEncoder,
                           UserDetailsService userDetailsService,
                           EmailService emailService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
    }

    private Date otpRequestedTime;

    long  otpRequestedTimeMillis=0;

    private static final long OTP_VALID_DURATION = 1 * 60 * 1000;

    public void setOtpRequestedTime(Date otpRequestedTime) {
        this.otpRequestedTime = otpRequestedTime;
    }

    @Override
    @Transactional
    public String genrateOTPAndSendOnEmail(String username) {
        Users customer=usersRepository.findByEmail(username);
        long otp= (long)(Math.random()*9000)+1000;
        customer.setOtp(otp);
        System.out.println(customer.getOtp());
        usersRepository.save(customer);
        System.out.println(customer);
        setOtpRequestedTime(new Date());
        otpRequestedTimeMillis = otpRequestedTime.getTime();
        return emailService.sendSimpleMail(new EmailDetails(username,"Your OTP for verification is "+otp,"Verify with OTP"));

    }

    @Override
    public boolean verifyOTP(long otp, String username) {
        Users customer=usersRepository.findByEmail(username);
        long currentTimeInMillis=System.currentTimeMillis();
        System.out.println("currentTimeInMillis:"+currentTimeInMillis);
        System.out.println("otpRequestedTimeMillis"+otpRequestedTimeMillis);
        if(otpRequestedTimeMillis + OTP_VALID_DURATION > currentTimeInMillis) {
            if(otp== customer.getOtp())
                return true;
            else
                return false;
        }else{
            return false;
        }

    }

    @Override
    public boolean findAlreadyExistUserByEmail(String email) {

        return usersRepository.existsByEmail(email);
    }

    @Override
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public void saveOrUpdate(Users newUser) {
        usersRepository.save(newUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Override
    public List<Users> findAll() {
        Optional<List<Users>> optionalUsersList = Optional.ofNullable(usersRepository.findAll());
        return optionalUsersList.orElse(new ArrayList<Users>());
    }

    @Override
    public boolean existById(long id) {
        return usersRepository.existsById((int) id);
    }

    @Override
    public Users findById(long id) {
        Optional<Users> optionalUsers = Optional.ofNullable(usersRepository.findById(id));
        return optionalUsers.orElse(new Users());
    }

    @Override
    public Users findUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public Users findUserByUsername(String username){
        return usersRepository.findUserByEmail(username);
    }
}
