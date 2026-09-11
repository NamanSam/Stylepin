package com.stylepin.repository;
import com.stylepin.entity.RefreshSession;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.Optional;
public interface RefreshSessionRepository extends JpaRepository<RefreshSession, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from RefreshSession s where s.id = :id")
    Optional<RefreshSession> lockById(@Param("id") String id);
    boolean existsByIdAndUserIdAndRevokedFalseAndExpiresAtAfter(String id, Long userId, Instant now);
    @Modifying
    @Query("delete from RefreshSession s where s.expiresAt < :now")
    void deleteExpired(@Param("now") Instant now);
}
