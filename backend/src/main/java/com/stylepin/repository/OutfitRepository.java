package com.stylepin.repository;

import com.stylepin.entity.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Long> {
    @org.springframework.data.jpa.repository.Query("select distinct o from Outfit o join fetch o.category left join fetch o.products p left join fetch p.category where o.id in :ids")
    java.util.List<Outfit> fetchProducts(@org.springframework.data.repository.query.Param("ids") java.util.List<Long> ids);
    @org.springframework.data.jpa.repository.Query("select distinct o from Outfit o left join fetch o.tags where o.id in :ids")
    java.util.List<Outfit> fetchTags(@org.springframework.data.repository.query.Param("ids") java.util.List<Long> ids);
    Optional<Outfit> findByTitle(String title);
}

