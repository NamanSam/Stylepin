package com.stylepin.service;

import com.stylepin.dto.*;
import com.stylepin.entity.*;
import com.stylepin.exception.ApiException;
import com.stylepin.repository.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.*;
import java.util.*;

@Service
public class AuthService {
    @jakarta.persistence.PersistenceContext
    private jakarta.persistence.EntityManager entityManager;
    private final UserRepository users;
    private final RefreshSessionRepository sessions;
    private final RefreshTokenRepository tokens;
    private final PasswordEncoder passwords;
    private final JwtEncoder encoder;
    private final String dummyHash;
    private final SecureRandom random = new SecureRandom();
    public AuthService(UserRepository users, RefreshSessionRepository sessions, RefreshTokenRepository tokens,
                       PasswordEncoder passwords, JwtEncoder encoder) {
        this.users = users; this.sessions = sessions; this.tokens = tokens;
        this.passwords = passwords; this.encoder = encoder;
        dummyHash = passwords.encode(UUID.randomUUID().toString());
    }
    public record LoginResult(AuthResponseDTO body, String refreshToken, Instant expiresAt) {
        @Override public String toString() { return "LoginResult[redacted]"; }
    }
    @Transactional
    public UserResponseDTO register(RegisterRequestDTO request) {
        checkPasswordBytes(request.password());
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        String username = request.username().trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmail(email) || users.existsByUsername(username))
            throw new ApiException(409, "Email or username already registered");
        return UserResponseDTO.from(users.saveAndFlush(new User(username, email, passwords.encode(request.password()))));
    }
    @Transactional
    public LoginResult login(LoginRequestDTO request, String oldToken) {
        checkPasswordBytes(request.password());
        User user = users.findByEmail(request.email().trim().toLowerCase(Locale.ROOT)).orElse(null);
        boolean matches = passwords.matches(request.password(), user == null ? dummyHash : user.getPasswordHash());
        if (user == null || !matches) throw new BadCredentialsException("Invalid credentials");
        revoke(oldToken);
        RefreshSession session = sessions.save(new RefreshSession(UUID.randomUUID().toString(), user,
            Instant.now().plus(Duration.ofDays(7))));
        return issue(session);
    }
    // Replay revocation must commit even when this method rejects the presented token.
    @Transactional(noRollbackFor = BadCredentialsException.class)
    public LoginResult refresh(String raw) {
        RefreshToken token = find(raw);
        if (token == null) throw new BadCredentialsException("Refresh required");
        RefreshSession session = sessions.lockById(token.getSession().getId()).orElseThrow();
        // Re-read after acquiring the session lock: concurrent refreshes must see the consumed flag.
        entityManager.refresh(token, jakarta.persistence.LockModeType.PESSIMISTIC_WRITE);
        boolean consumed = token.isConsumed();
        if (session.isRevoked() || !session.getExpiresAt().isAfter(Instant.now()) || consumed) {
            session.revoke();
            throw new BadCredentialsException("Refresh expired or reused");
        }
        token.consume();
        return issue(session);
    }
    @Transactional
    public void revoke(String raw) {
        RefreshToken token = find(raw);
        if (token != null) sessions.lockById(token.getSession().getId()).ifPresent(RefreshSession::revoke);
    }
    private RefreshToken find(String raw) {
        if (raw == null || !raw.matches("[A-Za-z0-9_-]{43}")) return null;
        return tokens.findById(hash(raw)).orElse(null);
    }
    private LoginResult issue(RefreshSession session) {
        byte[] bytes = new byte[32]; random.nextBytes(bytes);
        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        tokens.save(new RefreshToken(hash(raw), session));
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(900).isBefore(session.getExpiresAt()) ? now.plusSeconds(900) : session.getExpiresAt();
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("stylepin").audience(List.of("stylepin-web"))
            .subject(session.getUser().getId().toString()).issuedAt(now).expiresAt(expiry)
            .claim("sid", session.getId()).id(UUID.randomUUID().toString()).build();
        String access = encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
        return new LoginResult(new AuthResponseDTO(access, "Bearer", Duration.between(now,expiry).toSeconds(),
            UserResponseDTO.from(session.getUser())), raw, session.getExpiresAt());
    }
    private void checkPasswordBytes(String password) {
        if (password.getBytes(StandardCharsets.UTF_8).length > 72)
            throw new ApiException(400, "Password must not exceed 72 UTF-8 bytes");
    }
    private String hash(String raw) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8))); }
        catch (NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }
}
