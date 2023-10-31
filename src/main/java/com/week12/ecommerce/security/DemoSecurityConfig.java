//package com.week12.ecommerce.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//@Configuration
//public class DemoSecurityConfig {
//
//    @Autowired
//    private AuthenticationSuccessHandler customSuccessHandler; // Correctly inject the CustomSuccessHandler
//
//    @Bean
//    public InMemoryUserDetailsManager userDetailsManager() {
//        UserDetails suraj = User.builder()
//                .username("suraj")
//                .password("{noop}test123")
//                .roles("EMPLOYEE", "MANAGER", "ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(suraj);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .authorizeHttpRequests(configurer ->
//                        configurer
//                                .requestMatchers("/images/**","/css/**","/fonts/**","/vendor/**","/js/**").permitAll()
//                                .anyRequest().authenticated()
//                )
//                .formLogin(form ->
//                        form.loginPage("/showLogin")
//                                .loginPage("/showLogin")
//                                .loginProcessingUrl("/authenticateTheUser")
//                                .successHandler(customSuccessHandler) // Set the customSuccessHandler
//                                .permitAll());
//
//        return httpSecurity.build();
//    }
//}
