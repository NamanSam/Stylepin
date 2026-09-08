package com.stylepin.dto;

import com.stylepin.entity.Product;

import java.math.BigDecimal;

public class ProductResponseDTO {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private String imageUrl;
    private String productUrl;
    private String category;

    public ProductResponseDTO() {
    }

    public ProductResponseDTO(Long id, String name, String brand, BigDecimal price, String imageUrl, String productUrl, String category) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imageUrl = imageUrl;
        this.productUrl = productUrl;
        this.category = category;
    }

    public static ProductResponseDTO fromEntity(Product product) {
        if (product == null) {
            return null;
        }
        String categoryName = product.getCategory() != null ? product.getCategory().getName() : null;
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getPrice(),
                product.getImageUrl(),
                product.getProductUrl(),
                categoryName
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

