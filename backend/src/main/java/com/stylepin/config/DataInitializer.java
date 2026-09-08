package com.stylepin.config;

import com.stylepin.entity.Category;
import com.stylepin.entity.Outfit;
import com.stylepin.entity.Product;
import com.stylepin.repository.CategoryRepository;
import com.stylepin.repository.OutfitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final OutfitRepository outfitRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(OutfitRepository outfitRepository, CategoryRepository categoryRepository) {
        this.outfitRepository = outfitRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (outfitRepository.count() > 0) {
            return;
        }

        Category streetwearCat = categoryRepository.findBySlug("streetwear")
                .orElseGet(() -> categoryRepository.save(new Category("Minimal Streetwear", "streetwear", "Urban and streetwear styles")));

        Category oldMoneyCat = categoryRepository.findBySlug("old-money")
                .orElseGet(() -> categoryRepository.save(new Category("Old Money", "old-money", "Timeless classic and understated luxury")));

        Category koreanCasualCat = categoryRepository.findBySlug("korean-casual")
                .orElseGet(() -> categoryRepository.save(new Category("Korean Casual", "korean-casual", "Modern Seoul street and everyday casual")));

        Outfit streetwearOutfit = new Outfit(
                streetwearCat,
                "Minimal Streetwear",
                "Clean oversized streetwear fit featuring minimalist silhouette, neutral tones, and statement essentials.",
                "https://images.unsplash.com/photo-1552374196-1ab2a1c593e8?w=800"
        );
        streetwearOutfit.addTag("streetwear");
        streetwearOutfit.addTag("minimal");
        streetwearOutfit.addTag("oversized");

        Product hoodie = new Product(streetwearOutfit, "Oversized Heavyweight Hoodie", "Studio Raw", new BigDecimal("85.00"));
        hoodie.setImageUrl("https://images.unsplash.com/photo-1556905055-8f358a7a47b2?w=500");
        hoodie.setProductUrl("https://example.com/products/oversized-hoodie");
        hoodie.setCategory(streetwearCat);

        Product cargoPants = new Product(streetwearOutfit, "Wide-Leg Cargo Trousers", "Neutral Studios", new BigDecimal("95.00"));
        cargoPants.setImageUrl("https://images.unsplash.com/photo-1517445312882-bc9910d016b7?w=500");
        cargoPants.setProductUrl("https://example.com/products/cargo-trousers");
        cargoPants.setCategory(streetwearCat);

        Product sneakers = new Product(streetwearOutfit, "Retro Low Sneakers", "Common Grounds", new BigDecimal("130.00"));
        sneakers.setImageUrl("https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=500");
        sneakers.setProductUrl("https://example.com/products/retro-sneakers");
        sneakers.setCategory(streetwearCat);

        streetwearOutfit.addProduct(hoodie);
        streetwearOutfit.addProduct(cargoPants);
        streetwearOutfit.addProduct(sneakers);

        Outfit oldMoneyOutfit = new Outfit(
                oldMoneyCat,
                "Old Money",
                "Sophisticated quiet-luxury aesthetic with structured knitwear, pleated trousers, and artisan leather.",
                "https://images.unsplash.com/photo-1617137984095-74e4e5e3613f?w=800"
        );
        oldMoneyOutfit.addTag("oldmoney");
        oldMoneyOutfit.addTag("classic");
        oldMoneyOutfit.addTag("quietluxury");

        Product sweater = new Product(oldMoneyOutfit, "Cashmere Cable-Knit Sweater", "Heritage Club", new BigDecimal("185.00"));
        sweater.setImageUrl("https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=500");
        sweater.setProductUrl("https://example.com/products/cashmere-sweater");
        sweater.setCategory(oldMoneyCat);

        Product trousers = new Product(oldMoneyOutfit, "Pleated Wool-Blend Trousers", "Savile Tailors", new BigDecimal("145.00"));
        trousers.setImageUrl("https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=500");
        trousers.setProductUrl("https://example.com/products/pleated-trousers");
        trousers.setCategory(oldMoneyCat);

        Product loafers = new Product(oldMoneyOutfit, "Penny Leather Loafers", "Crown & Co", new BigDecimal("210.00"));
        loafers.setImageUrl("https://images.unsplash.com/photo-1533867617858-e7b97e060509?w=500");
        loafers.setProductUrl("https://example.com/products/leather-loafers");
        loafers.setCategory(oldMoneyCat);

        oldMoneyOutfit.addProduct(sweater);
        oldMoneyOutfit.addProduct(trousers);
        oldMoneyOutfit.addProduct(loafers);

        Outfit koreanCasualOutfit = new Outfit(
                koreanCasualCat,
                "Korean Casual",
                "Relaxed Korean contemporary style with boxy tailored layer, straight denim, and effortless accessories.",
                "https://images.unsplash.com/photo-1509631179647-0177331693ae?w=800"
        );
        koreanCasualOutfit.addTag("koreanfashion");
        koreanCasualOutfit.addTag("kcasual");
        koreanCasualOutfit.addTag("clean");

        Product blazer = new Product(koreanCasualOutfit, "Relaxed Fit Single-Breasted Blazer", "Seoul Studio", new BigDecimal("120.00"));
        blazer.setImageUrl("https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=500");
        blazer.setProductUrl("https://example.com/products/relaxed-blazer");
        blazer.setCategory(koreanCasualCat);

        Product denim = new Product(koreanCasualOutfit, "Straight-Cut Washed Denim", "Mono Denims", new BigDecimal("75.00"));
        denim.setImageUrl("https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=500");
        denim.setProductUrl("https://example.com/products/straight-denim");
        denim.setCategory(koreanCasualCat);

        Product bag = new Product(koreanCasualOutfit, "Minimal Canvas Messenger Bag", "Acro Objects", new BigDecimal("48.00"));
        bag.setImageUrl("https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500");
        bag.setProductUrl("https://example.com/products/canvas-bag");
        bag.setCategory(koreanCasualCat);

        koreanCasualOutfit.addProduct(blazer);
        koreanCasualOutfit.addProduct(denim);
        koreanCasualOutfit.addProduct(bag);

        outfitRepository.saveAll(List.of(streetwearOutfit, oldMoneyOutfit, koreanCasualOutfit));
    }
}