package com.stylepin.service;
import com.stylepin.dto.OutfitResponseDTO;
import com.stylepin.entity.Outfit;
import com.stylepin.repository.OutfitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
@Transactional(readOnly=true)
public class OutfitReadService {
    private final OutfitRepository outfits;
    public OutfitReadService(OutfitRepository outfits){this.outfits=outfits;}
    // Two collection queries avoid a products × tags Cartesian result and pagination over joins.
    public List<OutfitResponseDTO> load(List<Long> ids){
        if(ids.isEmpty())return List.of();
        var rows=outfits.fetchProducts(ids);
        outfits.fetchTags(ids);
        var map=rows.stream().collect(Collectors.toMap(Outfit::getId,Function.identity(),(a,b)->a));
        return ids.stream().filter(map::containsKey).map(id->OutfitResponseDTO.fromEntity(map.get(id))).toList();
    }
}
