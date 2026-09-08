package com.stylepin.dto;

import com.stylepin.entity.Outfit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OutfitResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String category;
    private Set<String> tags = new HashSet<>();
    private LocalDateTime createdAt;
    private List<ProductResponseDTO> products = new ArrayList<>();

    public OutfitResponseDTO() {
    }

    public OutfitResponseDTO(Long id, String title, String description, String imageUrl, String category, Set<String> tags, LocalDateTime createdAt, List<ProductResponseDTO> products) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.tags = tags != null ? tags : new HashSet<>();
        this.createdAt = createdAt;
        this.products = products != null ? products : new ArrayList<>();
    }

    public static OutfitResponseDTO fromEntity(Outfit outfit) {
        if (outfit == null) {
            return null;
        }

        String categoryName = outfit.getCategory() != null ? outfit.getCategory().getName() : null;
        Set<String> tagSet = outfit.getTags() != null ? new HashSet<>(outfit.getTags()) : new HashSet<>();
        List<ProductResponseDTO> productList = new ArrayList<>();
        if (outfit.getProducts() != null) {
            for (var product : outfit.getProducts()) {
                productList.add(ProductResponseDTO.fromEntity(product));
            }
        }

        return new OutfitResponseDTO(
                outfit.getId(),
                outfit.getTitle(),
                outfit.getDescription(),
                outfit.getImageUrl(),
                categoryName,
                tagSet,
                outfit.getCreatedAt(),
                productList
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ProductResponseDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponseDTO> products) {
        this.products = products;
    }
}

