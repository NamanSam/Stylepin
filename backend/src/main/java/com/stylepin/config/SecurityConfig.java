package com.stylepin.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.stylepin.repository.RefreshSessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.*;
import org.springframework.web.cors.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.*;

@Configuration
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(12); }
    @Bean SecretKey jwtKey(@Value("${stylepin.auth.jwt-secret}") String encoded) {
        byte[] key;
        try { key = Base64.getDecoder().decode(encoded); }
        catch (IllegalArgumentException e) { throw new IllegalStateException("JWT_SECRET must be Base64 encoded"); }
        if (key.length < 32) throw new IllegalStateException("JWT_SECRET must contain at least 32 random bytes");
        return new SecretKeySpec(key, "HmacSHA256");
    }
    @Bean JwtEncoder jwtEncoder(SecretKey key) { return new NimbusJwtEncoder(new ImmutableSecret<>(key)); }
    @Bean JwtDecoder jwtDecoder(SecretKey key, RefreshSessionRepository sessions) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
        OAuth2TokenValidator<Jwt> sessionValidator = jwt -> {
            try {
                if (jwt.getAudience().contains("stylepin-web") && jwt.getExpiresAt() != null &&
                    jwt.getExpiresAt().isAfter(Instant.now()) &&
                    sessions.existsByIdAndUserIdAndRevokedFalseAndExpiresAtAfter(
                        jwt.getClaimAsString("sid"), Long.valueOf(jwt.getSubject()), Instant.now()))
                    return OAuth2TokenValidatorResult.success();
            } catch (RuntimeException ignored) { /* Invalid claim or unavailable session: fail closed. */ }
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token"));
        };
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
            JwtValidators.createDefaultWithIssuer("stylepin"), sessionValidator));
        return decoder;
    }
    @Bean SecurityFilterChain security(HttpSecurity http, SecurityErrorHandler errors,
            com.stylepin.service.AdminAuthorizationService admins,
            @Value("${stylepin.auth.cookie-secure}") boolean secure,
            @Value("${stylepin.auth.allowed-origins}") String origins) throws Exception {
        CookieCsrfTokenRepository csrf = new CookieCsrfTokenRepository();
        csrf.setCookiePath("/api/auth");
        csrf.setCookieCustomizer(cookie -> cookie.httpOnly(true).secure(secure).sameSite("Strict"));
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(c -> c.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Arrays.stream(origins.split(",")).map(String::trim).toList());
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-XSRF-TOKEN"));
                config.setAllowCredentials(true);
                return config;
            }))
            // Only auth routes accept cookies as credentials. Bearer-only routes are not CSRF targets.
            .csrf(c -> c.csrfTokenRepository(csrf).requireCsrfProtectionMatcher(request ->
                request.getRequestURI().substring(request.getContextPath().length()).startsWith("/api/auth/") &&
                !List.of("GET", "HEAD", "OPTIONS").contains(request.getMethod())))
            .authorizeHttpRequests(a -> a
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/admin/**").access((authentication, context) ->
                    new org.springframework.security.authorization.AuthorizationDecision(admins.isAdmin(authentication.get())))
                .requestMatchers(HttpMethod.GET, "/api/outfits", "/api/outfits/*", "/api/auth/csrf").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login", "/api/auth/refresh", "/api/auth/logout").permitAll()
                .anyRequest().authenticated())
            .exceptionHandling(e -> e.authenticationEntryPoint((req,res,ex) -> errors.write(res,401))
                .accessDeniedHandler((req,res,ex) -> errors.write(res,403)))
            .oauth2ResourceServer(o -> o.jwt(j -> {}).authenticationEntryPoint((req,res,ex) -> errors.write(res,401))
                .accessDeniedHandler((req,res,ex) -> errors.write(res,403)))
            .httpBasic(b -> b.disable()).formLogin(f -> f.disable()).logout(l -> l.disable());
        return http.build();
    }
}
