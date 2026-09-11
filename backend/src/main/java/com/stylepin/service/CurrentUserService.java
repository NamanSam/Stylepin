package com.stylepin.service;
import com.stylepin.entity.User;
import com.stylepin.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
@Service
public class CurrentUserService {
    private final UserRepository users;
    public CurrentUserService(UserRepository users) { this.users = users; }
    public User get() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt))
            throw new BadCredentialsException("Sign in required");
        return users.findById(Long.valueOf(jwt.getSubject())).orElseThrow(() -> new BadCredentialsException("User unavailable"));
    }
}
