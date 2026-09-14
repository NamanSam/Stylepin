package com.stylepin.service;

import com.stylepin.entity.User;
import com.stylepin.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthorizationService {
    private final UserRepository users;
    public AdminAuthorizationService(UserRepository users) { this.users = users; }
    public boolean isAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() ||
                !(authentication.getPrincipal() instanceof Jwt jwt)) return false;
        try {
            return users.findById(Long.valueOf(jwt.getSubject()))
                .map(user -> user.getRole() == User.Role.ADMIN).orElse(false);
        } catch (NumberFormatException ex) { return false; }
    }
}
