package com.stylepin.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

    @Test
    @DisplayName("GET /api/outfits should return seeded outfits with products and categories")
    void shouldReturnAllOutfits() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/outfits")
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
                .andExpect(jsonPath("$[0].products[0].category").isNotEmpty())
                .andReturn();
    }
}