package com.stylepin.repository;
import com.stylepin.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("delete from RefreshToken t where t.session.id in (select s.id from RefreshSession s where s.expiresAt < :now)")
    void deleteExpired(@org.springframework.data.repository.query.Param("now") java.time.Instant now);
}
