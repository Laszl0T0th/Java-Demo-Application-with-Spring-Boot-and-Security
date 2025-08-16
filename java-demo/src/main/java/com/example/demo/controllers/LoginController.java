package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController {
	
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
    	System.out.println("/login - get");
        ModelAndView mav = new ModelAndView("login");
        if (error != null) {
        	logger.info("/login - get - error");
            mav.addObject("error", "Login attempt failed."); 
        }

        return mav; 
    }
    
    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username, @RequestParam String password) {
    	System.out.println("/login - post");
        if (isValidUser(username, password)) {
        	logger.info("/login - post - home");
            return new ModelAndView("home"); 
        } else {
        	logger.info("/login - post - error");
            return new ModelAndView("error");
        }
    }
    
    private boolean isValidUser(String username, String password) {
        return "test".equals(username);
    }
}
