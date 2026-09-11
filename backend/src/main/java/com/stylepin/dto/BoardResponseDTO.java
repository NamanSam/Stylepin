package com.stylepin.dto;
import com.stylepin.entity.Board;
import java.time.Instant;
public record BoardResponseDTO(Long id,String name,String description,Instant createdAt) {
    public static BoardResponseDTO from(Board b){return new BoardResponseDTO(b.getId(),b.getName(),b.getDescription(),b.getCreatedAt());}
}
