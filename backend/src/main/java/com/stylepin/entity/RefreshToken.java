package com.stylepin.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "refresh_tokens", indexes = @Index(columnList = "session_id"))
public class RefreshToken {
    @Id @Column(length = 64) private String tokenHash;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private RefreshSession session;
    @Column(nullable = false) private boolean consumed;
    protected RefreshToken() {}
    public RefreshToken(String hash, RefreshSession session) { tokenHash = hash; this.session = session; }
    public RefreshSession getSession() { return session; }
    public boolean isConsumed() { return consumed; }
    public void consume() { consumed = true; }
}
