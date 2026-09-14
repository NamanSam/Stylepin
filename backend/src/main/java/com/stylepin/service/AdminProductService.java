package com.stylepin.service;

import com.stylepin.dto.*;
import com.stylepin.entity.*;
import com.stylepin.repository.*;
import com.stylepin.exception.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class AdminProductService {
    private final ProductRepository products;
    private final CategoryRepository categories;
    public AdminProductService(ProductRepository products, CategoryRepository categories) {
        this.products = products; this.categories = categories;
    }
    public PageResponseDTO<AdminProductResponseDTO> list(int page, int size) {
        return PageResponseDTO.from(products.findAll(PageRequest.of(page, size,
            Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")))).map(AdminProductResponseDTO::from));
    }
    public List<String> categories() {
        return categories.findAll(Sort.by("name")).stream().map(Category::getName).toList();
    }
    @Transactional
    public AdminProductResponseDTO create(AdminProductRequestDTO request) {
        Product product = new Product();
        apply(product, request);
        return AdminProductResponseDTO.from(products.saveAndFlush(product));
    }
    @Transactional
    public AdminProductResponseDTO update(Long id, AdminProductRequestDTO request) {
        Product product = get(id);
        apply(product, request);
        return AdminProductResponseDTO.from(products.saveAndFlush(product));
    }
    @Transactional
    public void delete(Long id) {
        Product product = get(id);
        if (product.getOutfit() != null) throw new ApiException(409,
            "This product is attached to an outfit and cannot be deleted. You can mark it unavailable.");
        products.delete(product);
    }
    private Product get(Long id) {
        return products.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
    private void apply(Product product, AdminProductRequestDTO r) {
        String image = validateUrl(r.imageUrl(), false);
        String destination = validateUrl(r.productUrl(), true);
        String name = r.category().trim();
        Category category = categories.findByNameIgnoreCase(name).orElseGet(() ->
            categories.save(new Category(name, "product-" + UUID.randomUUID())));
        product.setName(r.name().trim()); product.setBrand(r.brand().trim());
        product.setPrice(r.price()); product.setImageUrl(image); product.setProductUrl(destination);
        product.setRetailer(r.retailer() == null ? null : r.retailer().trim());
        product.setCategory(category); product.setAvailable(r.available());
        Set<String> tags = new LinkedHashSet<>();
        if (r.tags() != null) r.tags().forEach(tag -> tags.add(tag.trim().toLowerCase(Locale.ROOT)));
        product.setTags(tags);
    }
    private String validateUrl(String value, boolean shopping) {
        try {
            URI uri = new URI(value.trim());
            String host = uri.getHost();
            if (host == null || !("https".equalsIgnoreCase(uri.getScheme()) || "http".equalsIgnoreCase(uri.getScheme()))
                || uri.getUserInfo() != null || uri.getPort() > 65535) throw new IllegalArgumentException();
            String normalizedHost = host.toLowerCase(Locale.ROOT).replaceAll("\\.$", "");
            if (shopping && (normalizedHost.equals("localhost") || normalizedHost.endsWith(".localhost") ||
                normalizedHost.endsWith(".invalid") || normalizedHost.endsWith(".test") || normalizedHost.endsWith(".example") ||
                List.of("example.com", "example.org", "example.net").stream()
                    .anyMatch(domain -> normalizedHost.equals(domain) || normalizedHost.endsWith("." + domain))))
                throw new IllegalArgumentException();
            return uri.toASCIIString();
        } catch (Exception ex) {
            throw new ApiException(400, shopping ? "Enter a valid HTTP(S) retailer URL, not a placeholder." :
                "Enter a valid HTTP(S) image URL.");
        }
    }
}
