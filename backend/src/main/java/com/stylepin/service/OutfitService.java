package com.stylepin.service;

import com.stylepin.dto.OutfitResponseDTO;
import com.stylepin.entity.Outfit;
import com.stylepin.exception.ResourceNotFoundException;
import com.stylepin.repository.OutfitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OutfitService {

    private final OutfitRepository outfitRepository;
    private final OutfitReadService reads;

    public OutfitService(OutfitRepository outfitRepository, OutfitReadService reads) {
        this.outfitRepository = outfitRepository;
        this.reads = reads;
    }

    public List<OutfitResponseDTO> getAllOutfits() {
        List<Long> ids = outfitRepository.findAll().stream().map(Outfit::getId).toList();
        List<OutfitResponseDTO> result = new java.util.ArrayList<>();
        for (int start = 0; start < ids.size(); start += 100) {
            result.addAll(reads.load(ids.subList(start, Math.min(start + 100, ids.size()))));
        }
        return result;
    }

    public OutfitResponseDTO getOutfitById(Long id) {
        return reads.load(List.of(id)).stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Outfit not found with id: " + id));
    }
}
