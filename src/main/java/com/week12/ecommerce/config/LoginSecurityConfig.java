package com.week12.ecommerce.config;

import com.week12.ecommerce.handlers.CustomSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class LoginSecurityConfig {
    private UserDetailsService userDetailsService;
    private CustomSuccessHandler customSuccessHandler;

    public LoginSecurityConfig(UserDetailsService userDetailsService,
                               CustomSuccessHandler customSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.customSuccessHandler = customSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeHttpRequests(configurer->
                        configurer
                                .requestMatchers("/signup/**","/user-registration","/images/**","/css/**","/fonts/**","/vendor/**","/js/**","/shop/**","/shop/category/**","/productImages/**").permitAll()
                                .requestMatchers("/verifyEmail","/forgotpassword","/checkout/**","/cart","/check-out/apply-coupon").permitAll()
                                .requestMatchers("/sendEmailOTPLogin","/forgotPasswordOTPLogin").permitAll()
                                .requestMatchers("/otpvalidation/**","/validateOTP/**").permitAll()
                                .requestMatchers("/sendVerificationEmailOtp","/forgot-password/**","/forgot-password/otpVerification/**","/passwordResetPage").permitAll()
                                .requestMatchers("/","/admin_panel","/user_home","/payNow/**","/user_home/sendReferralEmail","/signup/referral/**","user_home/referral","user-registration/referral").permitAll()
                                .anyRequest().authenticated()
                        ).formLogin(form->
                            form.loginPage("/login")
                                    .loginProcessingUrl("/authenticateTheUser")
                                    .successHandler(customSuccessHandler)
                                    .permitAll())
                        .logout(logout->
                            logout
                                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                    .permitAll());
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
