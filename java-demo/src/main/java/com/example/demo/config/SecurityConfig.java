package com.example.demo.config;

import com.example.demo.security.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration // Indicates that this class is a configuration class
@EnableWebSecurity // Enables Spring Security's web security support
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userService;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
    
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	System.out.println("------ securityFilterChain");
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());

        http.authorizeRequests(authorizeRequests -> {
                	authorizeRequests
                    .requestMatchers("/empolyees/**").hasRole("USER")
                    .requestMatchers("/home").hasRole("USER")
                    .requestMatchers("/login").permitAll() // Allows access to the login page for all users
                    .requestMatchers("/uploads/images/**").permitAll() // Allows access to static resources
                    .requestMatchers("/session-id").permitAll() 
                    .anyRequest().authenticated(); // Requires authentication for all other requests
            	})
                .formLogin(formLogin -> {
                    formLogin
                    .loginPage("/login") // Specifies the custom login page URL
                    .defaultSuccessUrl("/home", true)
                    .failureHandler(new CustomAuthenticationFailureHandler()) // Sets the custom authentication failure handler
                    .permitAll(); // Allows access to the login page for all users
                })
                .logout(logout ->
                logout
                		.logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Redirects to login page after logout
                        .invalidateHttpSession(true) // Invalidates the session
                        .deleteCookies("JSESSIONID") // Deletes the specified cookies
                        .permitAll() // Allows logout for all users
                )         
                .sessionManagement(sessionManagement ->
		        sessionManagement
                .sessionFixation().migrateSession()
		        .maximumSessions(1) 
                .maxSessionsPreventsLogin(true) 
		        .expiredUrl("/login?expired") // Redirect to this URL when session expires
		);

        return http.build(); // Builds the SecurityFilterChain object
    }
    
}
