package com.week12.ecommerce.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectURL = "/";
        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            if (grantedAuthority.getAuthority().equals("ADMIN")) {
                redirectURL = "/";
                break;
            }
            if (grantedAuthority.getAuthority().equals("CUSTOMER")) {
                redirectURL = "/";
                break;
            }

        }
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectURL);
    }
}
