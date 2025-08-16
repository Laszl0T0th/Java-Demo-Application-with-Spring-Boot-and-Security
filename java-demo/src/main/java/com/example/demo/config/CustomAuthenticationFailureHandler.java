package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
    
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

        request.getSession().setAttribute("errorMessage", exception.getMessage());

        System.out.println("Authentication failed: " + exception.getMessage());

        response.sendRedirect("/login?error=true");
	}

}
