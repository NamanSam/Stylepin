package com.stylepin.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record AdminProductRequestDTO(
    @NotBlank @Size(max=200) String name,
    @NotBlank @Size(max=100) String brand,
    @NotNull @DecimalMin("0.01") @Digits(integer=8, fraction=2) BigDecimal price,
    @NotBlank @Size(max=2048) String imageUrl,
    @NotBlank @Size(max=2048) String productUrl,
    @Size(max=100) String retailer,
    @NotBlank @Size(max=100) String category,
    @Size(max=30) List<@NotBlank @Size(max=50) String> tags,
    @NotNull Boolean available
) {}
