package com.example.demo.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@Configuration // Marks this class as a Spring configuration class
//@EnableWebMvc // Enables Spring's MVC support
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public MessageSource messageSource() {
        // Configures the MessageSource for internationalization support
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages"); // Sets the base name of the resource bundle
        messageSource.setDefaultEncoding("UTF-8"); // Sets the default encoding to UTF-8
        return messageSource; // Returns the configured MessageSource bean
    }

    @Bean
    public LocaleResolver localeResolver() {
        // Configures the LocaleResolver for determining the current locale
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US); // Sets the default locale to US
        return localeResolver; // Returns the configured LocaleResolver bean
    }
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
