package com.stylepin.service;

import com.stylepin.dto.OutfitResponseDTO;
import com.stylepin.entity.Outfit;
import com.stylepin.repository.OutfitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OutfitService {

    private final OutfitRepository outfitRepository;

    public OutfitService(OutfitRepository outfitRepository) {
        this.outfitRepository = outfitRepository;
    }

    public List<OutfitResponseDTO> getAllOutfits() {
        List<Outfit> outfits = outfitRepository.findAll();
        return outfits.stream()
                .map(OutfitResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

