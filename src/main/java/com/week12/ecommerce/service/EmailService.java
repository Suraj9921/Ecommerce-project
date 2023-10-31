package com.week12.ecommerce.service;

import com.week12.ecommerce.model.EmailDetails;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface EmailService {

    String sendSimpleMail(String email, String otp);


    String sendSimpleMail(EmailDetails verifyWithOtp);

    void sendReferralLink(String email, String referralLink) throws MessagingException, UnsupportedEncodingException;

}
