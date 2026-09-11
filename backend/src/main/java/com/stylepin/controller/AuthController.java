package com.stylepin.controller;
import com.stylepin.dto.*;
import com.stylepin.service.AuthService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String COOKIE = "stylepin_refresh";
    private final AuthService auth;
    private final boolean secure;
    public AuthController(AuthService auth, @Value("${stylepin.auth.cookie-secure}") boolean secure) {
        this.auth = auth; this.secure = secure;
    }
    @GetMapping("/csrf")
    public Map<String,String> csrf(CsrfToken token, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        return Map.of("token", token.getToken());
    }
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@Valid @RequestBody RegisterRequestDTO request) { return auth.register(request); }
    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request,
        @CookieValue(name = COOKIE, required = false) String old, HttpServletResponse response) {
        return respond(auth.login(request, old), response);
    }
    @PostMapping("/refresh")
    public AuthResponseDTO refresh(@CookieValue(name = COOKIE, required = false) String raw, HttpServletResponse response) {
        try { return respond(auth.refresh(raw), response); }
        catch (AuthenticationException ex) { cookie(response, "", Duration.ZERO); throw ex; }
    }
    @PostMapping("/logout") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@CookieValue(name = COOKIE, required = false) String raw, HttpServletResponse response) {
        auth.revoke(raw); cookie(response, "", Duration.ZERO);
    }
    private AuthResponseDTO respond(AuthService.LoginResult result, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        cookie(response, result.refreshToken(), Duration.between(Instant.now(), result.expiresAt()));
        return result.body();
    }
    private void cookie(HttpServletResponse response, String value, Duration age) {
        response.addHeader(HttpHeaders.SET_COOKIE, ResponseCookie.from(COOKIE, value).httpOnly(true)
            .secure(secure).sameSite("Strict").path("/api/auth").maxAge(age).build().toString());
    }
}
