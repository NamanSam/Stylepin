package com.stylepin.controller;

import com.stylepin.entity.Outfit;
import com.stylepin.repository.OutfitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OutfitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OutfitRepository outfitRepository;

    @Test
    @DisplayName("GET /api/outfits should return seeded outfits with products and categories")
    void shouldReturnAllOutfits() throws Exception {
        mockMvc.perform(get("/api/outfits")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").isNotEmpty())
                .andExpect(jsonPath("$[0].category").isNotEmpty())
                .andExpect(jsonPath("$[0].imageUrl").isNotEmpty())
                .andExpect(jsonPath("$[0].tags").isArray())
                .andExpect(jsonPath("$[0].products", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].products[0].id").exists())
                .andExpect(jsonPath("$[0].products[0].name").isNotEmpty())
                .andExpect(jsonPath("$[0].products[0].price").exists())
                .andExpect(jsonPath("$[0].products[0].category").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/outfits/{id} should return outfit detail when outfit exists")
    void shouldReturnOutfitByIdWhenExists() throws Exception {
        Outfit existingOutfit = outfitRepository.findAll().stream().findFirst().orElseThrow();
        Long id = existingOutfit.getId();

        mockMvc.perform(get("/api/outfits/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(existingOutfit.getTitle()))
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.imageUrl").isNotEmpty())
                .andExpect(jsonPath("$.category").isNotEmpty())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.products", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.products[0].id").exists())
                .andExpect(jsonPath("$.products[0].name").isNotEmpty())
                .andExpect(jsonPath("$.products[0].brand").isNotEmpty())
                .andExpect(jsonPath("$.products[0].price").exists())
                .andExpect(jsonPath("$.products[0].productUrl").isNotEmpty())
                .andExpect(jsonPath("$.products[0].category").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/outfits/{id} should return 404 when outfit does not exist")
    void shouldReturn404WhenOutfitDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/outfits/999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}