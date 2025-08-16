package com.example.demo.security.service;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
    
    @Autowired
    private UserRepository userRepository;

    @Override
	@Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	System.out.println("------ UserDetails loadUserByUsername");
        User user = userRepository.findByUsername(username);
        if (user == null) {
        	System.out.println("Username not found: " + username);
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        System.out.println("----- Loading user by username:");
        System.out.println("----- Username: " + username);
        System.out.println("----- User ID: " + user.getId());
        System.out.println("----- Email: " + user.getEmail());
        System.out.println("----- Enabled: " + user.isEnabled());
        System.out.println("----- Roles: " + user.getRoles().stream()
	            .map(role -> role.getName().name())
	            .collect(Collectors.joining(", ")));
	    
	    if (!user.isEnabled()) {
	    	System.out.println("User is not allowd: " + username);
	        throw new UsernameNotFoundException("User is not enabled.");
	    }

	    
	    List<GrantedAuthority> authorities = user.getRoles().stream()
	        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
	        .collect(Collectors.toList());

        System.out.println("----- Seccusse: " + user.getUsername());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                    .collect(Collectors.toList());
    }


}
