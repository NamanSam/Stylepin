package com.stylepin.dto;
public record BoardDetailResponseDTO(BoardResponseDTO board,PageResponseDTO<OutfitResponseDTO> outfits){}
