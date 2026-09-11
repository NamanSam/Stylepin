package com.stylepin.repository;
import com.stylepin.entity.SavedOutfit;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;
public interface SavedOutfitRepository extends JpaRepository<SavedOutfit,Long>{
    @Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SavedOutfit s where s.user.id=:id and s.outfit.id=:outfitId")
    java.util.Optional<SavedOutfit> lockMembership(@Param("id") Long id,@Param("outfitId") Long outfitId);
    @Modifying
    @Query("delete from SavedOutfit s where s.user.id=:id and s.outfit.id=:outfitId")
    void deleteByUserIdAndOutfitId(@Param("id") Long id,@Param("outfitId") Long outfitId);
    
    @Query(value="select s.outfit.id from SavedOutfit s where s.user.id=:id order by s.createdAt desc,s.id desc",
        countQuery="select count(s) from SavedOutfit s where s.user.id=:id")
    Page<Long> outfitIds(@Param("id") Long id,Pageable pageable);
}
