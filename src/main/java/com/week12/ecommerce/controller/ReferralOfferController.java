package com.week12.ecommerce.controller;


import com.week12.ecommerce.model.ReferralOffer;
import com.week12.ecommerce.model.Users;
import com.week12.ecommerce.security.CustomUser;
import com.week12.ecommerce.service.EmailService;
import com.week12.ecommerce.service.ReferralCodeGenerator;
import com.week12.ecommerce.service.ReferralOfferService;
import com.week12.ecommerce.service.UsersSevice;
import com.week12.ecommerce.utils.Utility;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Controller
@AllArgsConstructor
public class ReferralOfferController {
    private ReferralOfferService referralOfferService;
    private UsersSevice usersSevice;
    private ReferralCodeGenerator referralCodeGenerator;
    private EmailService emailService;
    @PostMapping("/user_home/sendReferralEmail")
    public String sendReferralEmail(@RequestParam("email") String email,
                                    HttpServletRequest request,
                                    Model model, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        if(usersSevice.existsByEmail(email)){
            model.addAttribute("message","Thia email id is already have an account");
        }else{
            //code to check already signup mail had sent

            String token = referralCodeGenerator.getToken(customUser.getId());
            if(referralOfferService.existsByEmail(email)){
                ReferralOffer referralOffer = referralOfferService.findBySenderMail(email);
                referralOffer.setReferralCode(token);
                referralOffer.setUsersId(customUser.getId());
                referralOffer.setSendDate(new Date());
                referralOfferService.update(referralOffer);
            }else{
                referralOfferService.saveReferral(customUser.getId(),token,email);
            }
            //code to send email
            String referralLink = Utility.getSiteURL(request)+"/signup/referral?token="+token;
            emailService.sendReferralLink(email,referralLink);
            model.addAttribute("message", "We have sent a referral link to this  email. Please check.");
        }
        return "ShareReferral";
    }
    @GetMapping("/signup/referral")
    public String showReferralSignup(@RequestParam("token") String token,Model model){
        ReferralOffer referralOffer = referralOfferService.findByReferralCode(token);
        model.addAttribute("token",token);
        if(referralOffer==null){
            model.addAttribute("message", "Invalid Token");
            return "login";
        }
        Users users = new Users();
        users.setEmail(referralOffer.getSenderEmail());
        model.addAttribute("newUsers",users);
        return "referralSignup";
    }
    @GetMapping("user_home/referral")
    public String showShareReferral(){
        return "ShareReferral";
    }
}
