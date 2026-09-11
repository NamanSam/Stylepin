package com.stylepin.repository;
import com.stylepin.entity.BoardOutfit;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;
public interface BoardOutfitRepository extends JpaRepository<BoardOutfit,Long>{
    @Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from BoardOutfit s where s.board.id=:id and s.outfit.id=:outfitId")
    java.util.Optional<BoardOutfit> lockMembership(@Param("id") Long id,@Param("outfitId") Long outfitId);
    @Modifying
    @Query("delete from BoardOutfit s where s.board.id=:id and s.outfit.id=:outfitId")
    void deleteByBoardIdAndOutfitId(@Param("id") Long id,@Param("outfitId") Long outfitId);
    @Modifying
    @Query("delete from BoardOutfit s where s.board.id=:id")
    void deleteByBoardId(@Param("id") Long id);
    @Query(value="select s.outfit.id from BoardOutfit s where s.board.id=:id order by s.createdAt desc,s.id desc",
        countQuery="select count(s) from BoardOutfit s where s.board.id=:id")
    Page<Long> outfitIds(@Param("id") Long id,Pageable pageable);
}
