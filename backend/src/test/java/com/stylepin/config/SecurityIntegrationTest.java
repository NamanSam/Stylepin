package com.stylepin.config;

import com.stylepin.dto.*;
import com.stylepin.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class SecurityIntegrationTest {
    @Autowired AuthService auth;
    @Autowired JwtEncoder encoder;
    @Autowired MockMvc mvc;
    @Autowired com.stylepin.repository.UserRepository users;
    @Autowired com.stylepin.repository.RefreshSessionRepository sessions;
    @Autowired com.stylepin.repository.RefreshTokenRepository refreshTokens;
    @Autowired com.stylepin.service.RefreshSessionCleanup cleanup;
    AuthService.LoginResult session() {
        String u = "u" + UUID.randomUUID().toString().replace("-","").substring(0,20);
        auth.register(new RegisterRequestDTO(u,u+"@example.com","secure long password"));
        return auth.login(new LoginRequestDTO(u+"@example.com","secure long password"),null);
    }
    @Test void expiredAndWrongAudienceTokensAreRejected() throws Exception {
        var session = session();
        var original = com.nimbusds.jwt.SignedJWT.parse(session.body().accessToken()).getJWTClaimsSet();
        for (boolean expired : List.of(true,false)) {
            var claims = JwtClaimsSet.builder().issuer("stylepin").subject(original.getSubject())
                .audience(List.of(expired ? "stylepin-web" : "another-app"))
                .claim("sid",original.getStringClaim("sid")).issuedAt(Instant.now().minusSeconds(1800))
                .expiresAt(expired ? Instant.now().minusSeconds(120) : Instant.now().plusSeconds(900)).build();
            String token = encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims)).getTokenValue();
            mvc.perform(get("/api/users/me").header("Authorization","Bearer "+token)).andExpect(status().isUnauthorized());
        }
    }
    @Test void concurrentRefreshCannotCreateTwoValidDescendants() throws Exception {
        var session = session();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch start = new CountDownLatch(1);
        try {
            Callable<Boolean> task = () -> {
                start.await();
                try { auth.refresh(session.refreshToken()); return true; }
                catch (org.springframework.security.core.AuthenticationException expected) { return false; }
            };
            var a = executor.submit(task); var b = executor.submit(task); start.countDown();
            assertNotEquals(a.get(15,TimeUnit.SECONDS),b.get(15,TimeUnit.SECONDS));
            mvc.perform(get("/api/users/me").header("Authorization","Bearer "+session.body().accessToken()))
                .andExpect(status().isUnauthorized());
        } finally { executor.shutdownNow(); }
    }
    @Test void expiredRefreshIsRejectedAndCleanupPreservesActiveSessions() throws Exception {
        var active = session();
        var user = users.findById(active.body().user().id()).orElseThrow();
        var expired = sessions.save(new com.stylepin.entity.RefreshSession(UUID.randomUUID().toString(),user,Instant.now().minusSeconds(60)));
        String raw = "A".repeat(43);
        String hash = java.util.HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256").digest(raw.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        refreshTokens.save(new com.stylepin.entity.RefreshToken(hash,expired));
        assertThrows(org.springframework.security.core.AuthenticationException.class,()->auth.refresh(raw));
        cleanup.removeExpired();
        assertFalse(sessions.existsById(expired.getId()));
        assertFalse(refreshTokens.existsById(hash));
        mvc.perform(get("/api/users/me").header("Authorization","Bearer "+active.body().accessToken())).andExpect(status().isOk());
    }
}
