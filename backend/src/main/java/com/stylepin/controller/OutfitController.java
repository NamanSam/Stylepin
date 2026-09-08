package com.stylepin.controller;

import com.stylepin.dto.OutfitResponseDTO;
import com.stylepin.service.OutfitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/outfits")
@CrossOrigin
public class OutfitController {

    private final OutfitService outfitService;

    public OutfitController(OutfitService outfitService) {
        this.outfitService = outfitService;
    }

    @GetMapping
    public ResponseEntity<List<OutfitResponseDTO>> getAllOutfits() {
        List<OutfitResponseDTO> outfits = outfitService.getAllOutfits();
        return ResponseEntity.ok(outfits);
    }
}

