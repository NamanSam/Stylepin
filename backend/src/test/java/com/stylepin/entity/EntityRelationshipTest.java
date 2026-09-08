package com.stylepin.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EntityRelationshipTest {

    @Test
    @DisplayName("Should maintain bidirectional relationship between Outfit and Product")
    void testOutfitProductBidirectionalRelationship() {
        Category category = new Category("Streetwear", "streetwear", "Urban fashion");
        Outfit outfit = new Outfit(category, "Oversized Hoodie Look", "https://images.unsplash.com/photo-1");

        Product hoodie = new Product(outfit, "Graphic Hoodie", "Urban Co", new BigDecimal("79.99"));
        Product cargoPants = new Product(outfit, "Cargo Pants", "StreetFit", new BigDecimal("59.50"));

        outfit.addProduct(hoodie);
        outfit.addProduct(cargoPants);

        assertEquals(2, outfit.getProducts().size());
        assertSame(outfit, hoodie.getOutfit());
        assertSame(outfit, cargoPants.getOutfit());

        outfit.removeProduct(hoodie);
        assertEquals(1, outfit.getProducts().size());
        assertNull(hoodie.getOutfit());
    }

    @Test
    @DisplayName("Should handle Category and Outfit relationship")
    void testCategoryOutfitRelationship() {
        Category category = new Category("Casual", "casual");
        Outfit outfit1 = new Outfit(category, "Summer Linen", "https://images.unsplash.com/photo-2");
        Outfit outfit2 = new Outfit(category, "Denim Jacket Fit", "https://images.unsplash.com/photo-3");

        category.getOutfits().add(outfit1);
        category.getOutfits().add(outfit2);

        assertEquals(2, category.getOutfits().size());
        assertEquals("casual", outfit1.getCategory().getSlug());
        assertEquals("Casual", outfit2.getCategory().getName());
    }

    @Test
    @DisplayName("Should handle Outfit tags manipulation")
    void testOutfitTags() {
        Category category = new Category("Minimalist", "minimalist");
        Outfit outfit = new Outfit(category, "Clean White Tee", "https://images.unsplash.com/photo-4");

        outfit.addTag("OOTD");
        outfit.addTag("streetstyle");
        outfit.addTag("  Minimal ");

        Set<String> tags = outfit.getTags();
        assertEquals(3, tags.size());
        assertTrue(tags.contains("ootd"));
        assertTrue(tags.contains("streetstyle"));
        assertTrue(tags.contains("minimal"));

        outfit.removeTag("OOTD");
        assertFalse(outfit.getTags().contains("ootd"));
        assertEquals(2, outfit.getTags().size());
    }
}
