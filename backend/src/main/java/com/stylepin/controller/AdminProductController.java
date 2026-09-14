package com.stylepin.controller;
import com.stylepin.dto.*;
import com.stylepin.service.AdminProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController @Validated @RequestMapping("/api/admin")
public class AdminProductController {
    private final AdminProductService products;
    public AdminProductController(AdminProductService products) { this.products = products; }
    @GetMapping("/products")
    public PageResponseDTO<AdminProductResponseDTO> list(@RequestParam(defaultValue="0") @Min(0) int page,
        @RequestParam(defaultValue="24") @Min(1) @Max(100) int size) { return products.list(page, size); }
    @PostMapping("/products") @ResponseStatus(HttpStatus.CREATED)
    public AdminProductResponseDTO create(@Valid @RequestBody AdminProductRequestDTO request) { return products.create(request); }
    @PutMapping("/products/{id}")
    public AdminProductResponseDTO update(@PathVariable @Positive Long id, @Valid @RequestBody AdminProductRequestDTO request) {
        return products.update(id, request);
    }
    @DeleteMapping("/products/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) { products.delete(id); }
    @GetMapping("/categories") public List<String> categories() { return products.categories(); }
}
