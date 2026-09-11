package com.stylepin.service;
import com.stylepin.repository.RefreshSessionRepository;
import com.stylepin.repository.RefreshTokenRepository;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service @EnableScheduling
public class RefreshSessionCleanup {
    private final RefreshTokenRepository tokens;
    private final RefreshSessionRepository sessions;
    public RefreshSessionCleanup(RefreshTokenRepository tokens, RefreshSessionRepository sessions) {
        this.tokens = tokens; this.sessions = sessions;
    }
    // Keep consumed tokens until absolute session expiry so reuse can revoke descendants.
    @Scheduled(fixedDelay = 3600000, initialDelay = 3600000)
    @Transactional
    public void removeExpired() {
        Instant now = Instant.now();
        tokens.deleteExpired(now);
        sessions.deleteExpired(now);
    }
}
