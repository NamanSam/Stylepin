package com.stylepin.repository;
import com.stylepin.entity.Board;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;
import jakarta.persistence.LockModeType;
import java.util.Optional;
public interface BoardRepository extends JpaRepository<Board,Long>{
    Page<Board> findByOwnerId(Long ownerId,Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select b from Board b where b.id=:id")
    Optional<Board> lockById(@Param("id") Long id);
}
