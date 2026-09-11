package com.stylepin.service;
import com.stylepin.dto.*;
import com.stylepin.entity.SavedOutfit;
import com.stylepin.repository.*;
import com.stylepin.exception.ResourceNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class SavedOutfitService {
    private final SavedOutfitRepository saves;
    private final OutfitRepository outfits;
    private final UserRepository users;
    private final CurrentUserService current;
    private final OutfitReadService reads;
    public SavedOutfitService(SavedOutfitRepository saves,OutfitRepository outfits,UserRepository users,CurrentUserService current,OutfitReadService reads){
        this.saves=saves;this.outfits=outfits;this.users=users;this.current=current;this.reads=reads;
    }
    @Transactional public void save(Long outfitId){
        var user=users.lockById(current.get().getId()).orElseThrow();
        var outfit=outfits.findById(outfitId).orElseThrow(()->new ResourceNotFoundException("Outfit not found"));
        if(saves.lockMembership(user.getId(),outfitId).isEmpty())saves.save(new SavedOutfit(user,outfit));
    }
    @Transactional public void remove(Long outfitId){
        var user=users.lockById(current.get().getId()).orElseThrow();
        saves.deleteByUserIdAndOutfitId(user.getId(),outfitId);
    }
    @Transactional(readOnly=true) public PageResponseDTO<OutfitResponseDTO> list(int page,int size){
        var ids=saves.outfitIds(current.get().getId(),PageRequest.of(page,size));
        return new PageResponseDTO<>(reads.load(ids.getContent()),page,size,ids.getTotalElements(),ids.getTotalPages());
    }
}
