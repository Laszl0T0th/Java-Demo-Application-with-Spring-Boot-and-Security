package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;


@Configuration
@EnableAsync
public class Config {
	
    private static final Logger logger = LoggerFactory.getLogger(Config.class);


}

