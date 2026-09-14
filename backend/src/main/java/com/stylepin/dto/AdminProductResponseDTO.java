package com.stylepin.dto;
import com.stylepin.entity.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record AdminProductResponseDTO(Long id, String name, String brand, BigDecimal price,
    String imageUrl, String productUrl, String retailer, String category, Set<String> tags,
    boolean available, Long outfitId, LocalDateTime createdAt) {
    public static AdminProductResponseDTO from(Product p) {
        return new AdminProductResponseDTO(p.getId(), p.getName(), p.getBrand(), p.getPrice(),
            p.getImageUrl(), p.getProductUrl(), p.getRetailer(),
            p.getCategory() == null ? null : p.getCategory().getName(), Set.copyOf(p.getTags()),
            p.isAvailable(), p.getOutfit() == null ? null : p.getOutfit().getId(), p.getCreatedAt());
    }
}
