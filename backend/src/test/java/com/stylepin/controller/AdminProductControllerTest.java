package com.stylepin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylepin.dto.*;
import com.stylepin.service.AuthService;
import com.stylepin.repository.*;
import com.stylepin.config.DataInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.MediaType;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class AdminProductControllerTest {
    @Autowired MockMvc mvc;
    @Autowired AuthService auth;
    @Autowired JdbcTemplate jdbc;
    @Autowired ObjectMapper json;
    @Autowired ProductRepository products;
    @Autowired OutfitRepository outfits;
    @Autowired DataInitializer seed;
    AuthService.LoginResult session(boolean admin) {
        String username = "p" + UUID.randomUUID().toString().replace("-", "").substring(0,20);
        var user = auth.register(new RegisterRequestDTO(username, username + "@example.com", "secure testing password"));
        assertEquals(com.stylepin.entity.User.Role.USER, user.role());
        if (admin) jdbc.update("update users set role='ADMIN' where id=?", user.id());
        return auth.login(new LoginRequestDTO(username + "@example.com", "secure testing password"), null);
    }
    Map<String,Object> body() {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("name", "Test garment"); body.put("brand", "Test brand"); body.put("price", 2990);
        body.put("imageUrl", "https://images.unsplash.com/test-image");
        body.put("productUrl", "https://www.zara.com/in/en/test-product.html");
        body.put("retailer", "Test retailer"); body.put("category", "Test jacket");
        body.put("tags", List.of(" Minimal ", "casual")); body.put("available", true);
        return body;
    }
    @Test void allAdminRoutesRequireAdminAndRevocationIsImmediate() throws Exception {
        String ordinary = session(false).body().accessToken();
        var admin = session(true);
        for (String token : List.of("", ordinary)) {
            int status = token.isEmpty() ? 401 : 403;
            for (var request : List.of(get("/api/admin/products"), get("/api/admin/categories"),
                    post("/api/admin/products"), put("/api/admin/products/1"), delete("/api/admin/products/1"))) {
                if (!token.isEmpty()) request.header("Authorization", "Bearer " + token);
                mvc.perform(request.contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(body())))
                    .andExpect(status().is(status));
            }
        }
        mvc.perform(get("/api/admin/products").header("Authorization", "Bearer " + admin.body().accessToken())).andExpect(status().isOk());
        jdbc.update("update users set role='USER' where id=?", admin.body().user().id());
        mvc.perform(get("/api/admin/products").header("Authorization", "Bearer " + admin.body().accessToken())).andExpect(status().isForbidden());
    }
    @Test void createListUpdateDeletePersistsAndSeedingPreservesChanges() throws Exception {
        String bearer = "Bearer " + session(true).body().accessToken();
        long outfitCount = outfits.count();
        var response = mvc.perform(post("/api/admin/products").header("Authorization", bearer)
            .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(body())))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.outfitId").isEmpty())
            .andExpect(jsonPath("$.tags").isArray()).andReturn();
        long id = json.readTree(response.getResponse().getContentAsString()).get("id").asLong();
        assertEquals("Test garment", products.findById(id).orElseThrow().getName());
        mvc.perform(get("/api/admin/products").header("Authorization", bearer)).andExpect(status().isOk())
            .andExpect(jsonPath("$.items[0].id").value(id));
        var changed = body(); changed.put("name", "Edited garment"); changed.put("available", false); changed.put("price", 3199.50);
        mvc.perform(put("/api/admin/products/"+id).header("Authorization", bearer)
            .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(changed)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.available").value(false));
        long count = products.count(); seed.run(); seed.run();
        assertEquals(count, products.count()); assertEquals(outfitCount, outfits.count());
        assertEquals("Edited garment", products.findById(id).orElseThrow().getName());
        mvc.perform(delete("/api/admin/products/"+id).header("Authorization", bearer)).andExpect(status().isNoContent());
        assertFalse(products.existsById(id));
        mvc.perform(put("/api/admin/products/"+id).header("Authorization", bearer)
            .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(body()))).andExpect(status().isNotFound());
    }
    @Test void attachedProductChangesSurviveSeedAndDeletionIsBlocked() throws Exception {
        String bearer = "Bearer " + session(true).body().accessToken();
        Long id = jdbc.queryForObject("select min(id) from products where outfit_id is not null", Long.class);
        var original = jdbc.queryForMap("select name, brand, price, image_url, product_url, category_id, retailer, available from products where id=?", id);
        try {
            var changed = body(); changed.put("name", "Manually edited attached piece");
            mvc.perform(put("/api/admin/products/"+id).header("Authorization", bearer)
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(changed))).andExpect(status().isOk());
            seed.run();
            assertEquals("Manually edited attached piece", products.findById(id).orElseThrow().getName());
            mvc.perform(delete("/api/admin/products/"+id).header("Authorization", bearer)).andExpect(status().isConflict());
            mvc.perform(get("/api/outfits")).andExpect(status().isOk());
        } finally {
            jdbc.update("update products set name=?,brand=?,price=?,image_url=?,product_url=?,category_id=?,retailer=?,available=? where id=?",
                original.get("name"), original.get("brand"), original.get("price"), original.get("image_url"), original.get("product_url"), original.get("category_id"), original.get("retailer"), original.get("available"), id);
            jdbc.update("delete from product_tags where product_id=?", id);
        }
    }
    @Test void validatesRequiredFieldsPricesUrlsAndPagination() throws Exception {
        String bearer = "Bearer " + session(true).body().accessToken();
        for (String field : List.of("name", "brand", "price", "imageUrl", "productUrl", "category", "available")) {
            var invalid = body(); invalid.remove(field);
            mvc.perform(post("/api/admin/products").header("Authorization", bearer).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(invalid))).andExpect(status().isBadRequest());
        }
        for (String url : List.of("javascript:alert(1)", "https://example.com/item", "https://shop.example.com/item", "https://example.com./item", "https://shop.example/item", "https://shop.com:99999/item", "https://user:pass@shop.com/item", "not a url")) {
            var invalid = body(); invalid.put("productUrl", url);
            mvc.perform(post("/api/admin/products").header("Authorization", bearer).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(invalid))).andExpect(status().isBadRequest());
        }
        for (Object price : List.of(-1, 0, 100000000, 2.999)) {
            var invalid = body(); invalid.put("price", price);
            mvc.perform(post("/api/admin/products").header("Authorization", bearer).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(invalid))).andExpect(status().isBadRequest());
        }
        mvc.perform(get("/api/admin/products?size=101").header("Authorization", bearer)).andExpect(status().isBadRequest());
        mvc.perform(options("/api/admin/products/1").header("Origin", "http://localhost:5173")
            .header("Access-Control-Request-Method", "PUT")).andExpect(status().isOk());
    }
}
