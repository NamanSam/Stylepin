package com.stylepin.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_sessions", indexes = @Index(columnList = "user_id"))
public class RefreshSession {
    @Id private String id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false) private Instant expiresAt;
    @Column(nullable = false) private boolean revoked;
    protected RefreshSession() {}
    public RefreshSession(String id, User user, Instant expiresAt) {
        this.id = id; this.user = user; this.expiresAt = expiresAt;
    }
    public String getId() { return id; }
    public User getUser() { return user; }
    public Instant getExpiresAt() { return expiresAt; }
    public boolean isRevoked() { return revoked; }
    public void revoke() { revoked = true; }
}
