package com.stylepin.config;

import com.stylepin.entity.Category;
import com.stylepin.entity.Outfit;
import com.stylepin.entity.Product;
import com.stylepin.repository.CategoryRepository;
import com.stylepin.repository.OutfitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "stylepin.seed-data.enabled", havingValue = "true", matchIfMissing = true)
public class DataInitializer implements CommandLineRunner {

    private static final String IMAGE_BASE = "https://images.unsplash.com/";

    private final OutfitRepository outfitRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(OutfitRepository outfitRepository, CategoryRepository categoryRepository) {
        this.outfitRepository = outfitRepository;
        this.categoryRepository = categoryRepository;
    }

    private record ProductSeed(String name, String brand, int priceInr, String imageId, String slug) {
    }

    @Override
    @Transactional
    public void run(String... args) {
        Category streetwear = ensureCategory("Streetwear", "streetwear", "Urban and streetwear styles");
        Category oldMoney = ensureCategory("Old Money", "old-money", "Timeless classic and understated luxury");
        Category koreanCasual = ensureCategory("Korean Casual", "korean-casual", "Modern Seoul street and everyday casual");
        Category y2k = ensureCategory("Y2K", "y2k", "Bold early-2000s nostalgia");
        Category minimal = ensureCategory("Minimal", "minimal", "Clean lines and quiet neutrals");
        Category college = ensureCategory("College", "college", "Campus-ready laid-back classics");
        Category vintage = ensureCategory("Vintage", "vintage", "Retro pieces with archive appeal");
        Category casual = ensureCategory("Casual", "casual", "Everyday comfort without trying");
        Category formal = ensureCategory("Formal", "formal", "Sharp tailoring and evening polish");
        Category summer = ensureCategory("Summer", "summer", "Light layers for warm days");
        Category winter = ensureCategory("Winter", "winter", "Heavy layers for cold weather");
        Category monochrome = ensureCategory("Monochrome", "monochrome", "Single-tone styling");
        Category denim = ensureCategory("Denim", "denim", "Denim-first looks");
        Category oversized = ensureCategory("Oversized", "oversized", "Relaxed, exaggerated silhouettes");

        List<Outfit> outfits = new ArrayList<>();

        seedOutfit(outfits, streetwear, "Minimal Streetwear",
                "Clean oversized streetwear fit featuring minimalist silhouette, neutral tones, and statement essentials.",
                img("photo-1552374196-1ab2a1c593e8"),
                List.of("streetwear", "minimal", "oversized"),
                List.of(
                        product("Oversized Heavyweight Hoodie", "Studio Raw", 1499, "photo-1556905055-8f358a7a47b2", "oversized-hoodie"),
                        product("Wide-Leg Cargo Trousers", "Neutral Studios", 2299, "photo-1517445312882-bc9910d016b7", "cargo-trousers"),
                        product("Retro Low Sneakers", "Common Grounds", 3999, "photo-1595950653106-6c9ebd614d3a", "retro-low-sneakers")
                ));

        seedOutfit(outfits, oldMoney, "Old Money",
                "Sophisticated quiet-luxury aesthetic with structured knitwear, pleated trousers, and artisan leather.",
                img("photo-1617137984095-74e4e5e3613f"),
                List.of("oldmoney", "classic", "quietluxury"),
                List.of(
                        product("Cashmere Cable-Knit Sweater", "Heritage Club", 8999, "photo-1620799140408-edc6dcb6d633", "cashmere-sweater"),
                        product("Pleated Wool-Blend Trousers", "Savile Tailors", 5999, "photo-1473966968600-fa801b869a1a", "pleated-wool-trousers"),
                        product("Penny Leather Loafers", "Crown & Co", 6499, "photo-1533867617858-e7b97e060509", "penny-leather-loafers")
                ));

        seedOutfit(outfits, koreanCasual, "Korean Casual",
                "Relaxed Korean contemporary style with boxy tailored layer, straight denim, and effortless accessories.",
                img("photo-1509631179647-0177331693ae"),
                List.of("koreanfashion", "kcasual", "clean"),
                List.of(
                        product("Relaxed Single-Breasted Blazer", "Seoul Studio", 4999, "photo-1507679799987-c73779587ccf", "relaxed-blazer"),
                        product("Straight-Cut Washed Denim", "Mono Denims", 2799, "photo-1541099649105-f69ad21f3246", "straight-washed-denim"),
                        product("Canvas Messenger Bag", "Acro Objects", 1899, "photo-1553062407-98eeb64c6a62", "canvas-messenger-bag")
                ));

        seedOutfit(outfits, streetwear, "Boxy Street Fit",
                "A boxy, street-ready layer over relaxed cargos, finished with chunky soles and a tonal cap.",
                img("photo-1529139574466-a303027c1d8b"),
                List.of("streetwear", "boxy", "bomber"),
                List.of(
                        product("Cropped Bomber Jacket", "Basement Label", 4499, "photo-1591047139829-d91aecb6caea", "cropped-bomber-jacket"),
                        product("Relaxed Cargo Trousers", "Metro Supply", 2499, "photo-1517445312882-bc9910d016b7", "relaxed-cargo-trousers"),
                        product("Chunky White Sneakers", "Common Grounds", 4799, "photo-1556906781-9a412961c28c", "chunky-white-sneakers"),
                        product("Tonal Strap Cap", "Acro Objects", 799, "photo-1525507119028-ed4c629a60a3", "tonal-strap-cap")
                ));

        seedOutfit(outfits, streetwear, "Urban Neon Nights",
                "Late-night city energy with a reflective down layer, dark cargos, and muted neon accents.",
                img("photo-1554568218-0f1715e72254"),
                List.of("streetwear", "night", "neon"),
                List.of(
                        product("Reflective Down Jacket", "Afterdark", 6999, "photo-1591047139829-d91aecb6caea", "reflective-down-jacket"),
                        product("Black Slim Cargos", "Metro Supply", 2299, "photo-1517445312882-bc9910d016b7", "black-slim-cargos"),
                        product("Laceless Runner Sneakers", "Common Grounds", 4499, "photo-1556906781-9a412961c28c", "laceless-runner-sneakers")
                ));

        seedOutfit(outfits, streetwear, "Sneaker Rotation",
                "Built around the rotation: a favorite pair of sneakers styled with an oversized tee and technical joggers.",
                img("photo-1556906781-9a412961c28c"),
                List.of("streetwear", "sneakers", "casual"),
                List.of(
                        product("Oversized Graphic Tee", "Basement Label", 1199, "photo-1571945153237-4929e783af4a", "oversized-graphic-tee"),
                        product("Technical Joggers", "Metro Supply", 2699, "photo-1517445312882-bc9910d016b7", "technical-joggers"),
                        product("Retro Running Sneakers", "Common Grounds", 5999, "photo-1556906781-9a412961c28c", "retro-running-sneakers")
                ));

        seedOutfit(outfits, oldMoney, "Heritage Golf Day",
                "Classic preppy golf look with a fine knit polo, pleated shorts, and timeless leather belt.",
                img("photo-1488161628813-04466f872be2"),
                List.of("oldmoney", "classic", "preppy"),
                List.of(
                        product("Fine-Knit Polo Shirt", "Heritage Club", 3499, "photo-1536700503339-1e4b06520771", "fine-knit-polo"),
                        product("Pleated Golf Shorts", "Savile Tailors", 2299, "photo-1473966968600-fa801b869a1a", "pleated-golf-shorts"),
                        product("Classic Leather Belt", "Crown & Co", 1499, "photo-1548036328-c9fa89d128fa", "classic-leather-belt")
                ));

        seedOutfit(outfits, oldMoney, "Quiet Luxury Tailoring",
                "A softly-tailored jacket over silk-blend basics in muted earth tones for effortless polish.",
                img("photo-1507003211169-0a1dd7228f2d"),
                List.of("oldmoney", "tailoring", "quietluxury"),
                List.of(
                        product("Soft-Shoulder Tailored Jacket", "Savile Tailors", 8499, "photo-1507679799987-c73779587ccf", "soft-shoulder-jacket"),
                        product("Silk-Blend Crewneck", "Heritage Club", 4499, "photo-1539002847431-2e54497d5ced", "silk-blend-crewneck"),
                        product("Dress Leather Watch", "Crown & Co", 7999, "photo-1523170335258-f5ed11844a49", "dress-leather-watch")
                ));

        seedOutfit(outfits, koreanCasual, "Seoul Street Easy",
                "Easy Seoul layering with a lightweight overshirt over a tee, finished with tapered trousers.",
                img("photo-1495385794356-15371f348c31"),
                List.of("koreanfashion", "streetwear", "easy"),
                List.of(
                        product("Lightweight Overshirt", "Seoul Studio", 2899, "photo-1521577352947-9bb58764b69a", "lightweight-overshirt"),
                        product("Basic Cotton Tee", "Seoul Studio", 799, "photo-1511556820780-d912e42b4980", "basic-cotton-tee"),
                        product("Tapered Linen Trousers", "Mono Denims", 2399, "photo-1473966968600-fa801b869a1a", "tapered-linen-trousers"),
                        product("Crossbody Sling Bag", "Acro Objects", 1299, "photo-1548036328-c9fa89d128fa", "crossbody-sling-bag")
                ));

        seedOutfit(outfits, koreanCasual, "Box-fit Campus Layer",
                "Boxy campus fit that layers a coach jacket over a hoodie with straight denim and white sneakers.",
                img("photo-1503342217505-b0a15ec3261c"),
                List.of("koreanfashion", "campus", "layering"),
                List.of(
                        product("Coach Jacket", "Campus Classics", 3999, "photo-1591047139829-d91aecb6caea", "coach-jacket"),
                        product("Fleece Hoodie", "Neutral Studios", 2099, "photo-1556905055-8f358a7a47b2", "fleece-hoodie"),
                        product("Straight-Leg Denim", "Mono Denims", 2699, "photo-1541099649105-f69ad21f3246", "straight-leg-denim"),
                        product("White Court Sneakers", "Common Grounds", 3799, "photo-1595950653106-6c9ebd614d3a", "white-court-sneakers")
                ));

        seedOutfit(outfits, y2k, "Y2K Flash Party",
                "All the flash of the early 2000s: metallic top, low-rise denim, and chunky platform soles.",
                img("photo-1469334031218-e382a71b716b"),
                List.of("y2k", "party", "flash"),
                List.of(
                        product("Metallic Halter Top", "Neon Archive", 1899, "photo-1511556820780-d912e42b4980", "metallic-halter-top"),
                        product("Low-Rise Flare Denim", "Retro Flash", 3199, "photo-1541099649105-f69ad21f3246", "low-rise-flare-denim"),
                        product("Chunky Platform Sneakers", "Neon Archive", 5299, "photo-1595950653106-6c9ebd614d3a", "chunky-platform-sneakers"),
                        product("Mini Slouch Bag", "Retro Flash", 1799, "photo-1548036328-c9fa89d128fa", "mini-slouch-bag")
                ));

        seedOutfit(outfits, y2k, "Metallic Mini Edit",
                "A bright metallic mini skirt paired with a ribbed crop top and glossy accessories.",
                img("photo-1520975954732-35dd22299614"),
                List.of("y2k", "metallic", "street"),
                List.of(
                        product("Ribbed Crop Top", "Neon Archive", 999, "photo-1511556820780-d912e42b4980", "ribbed-crop-top"),
                        product("Metallic Mini Skirt", "Retro Flash", 2499, "photo-1517445312882-bc9910d016b7", "metallic-mini-skirt"),
                        product("Glossy Frame Sunglasses", "Neon Archive", 1299, "photo-1485841890310-6a055c88698a", "glossy-frame-sunglasses")
                ));

        seedOutfit(outfits, y2k, "Cyber Throwback",
                "Internet-era nostalgia with a vintage tech tee, distressed denim, and translucent sneakers.",
                img("photo-1519230226635-1015a79b735a"),
                List.of("y2k", "cyber", "denim"),
                List.of(
                        product("Vintage Tech Tee", "Retro Flash", 1399, "photo-1571945153237-4929e783af4a", "vintage-tech-tee"),
                        product("Distressed Baggy Denim", "Retro Flash", 2999, "photo-1541099649105-f69ad21f3246", "distressed-baggy-denim"),
                        product("Translucent Runner Sneakers", "Neon Archive", 3699, "photo-1556906781-9a412961c28c", "translucent-runner-sneakers")
                ));

        seedOutfit(outfits, minimal, "Quiet Beige Minimal",
                "Head-to-toe beige in relaxed tailoring for a quiet, refined everyday uniform.",
                img("photo-1496747611176-843222e1e57c"),
                List.of("minimal", "beige", "relaxed"),
                List.of(
                        product("Beige Relaxed Shirt", "Studio Raw", 2599, "photo-1521577352947-9bb58764b69a", "beige-relaxed-shirt"),
                        product("Essential Cargo Pants", "Neutral Studios", 2799, "photo-1517445312882-bc9910d016b7", "essential-cargo-pants"),
                        product("Smooth Leather Loafers", "Crown & Co", 4999, "photo-1533867617858-e7b97e060509", "smooth-leather-loafers")
                ));

        seedOutfit(outfits, minimal, "Minimal Studio White",
                "Crisp white and off-white layers that keep the silhouette clean from collar to cuff.",
                img("photo-1445205170230-053b83016050"),
                List.of("minimal", "white", "clean"),
                List.of(
                        product("Clean White Tee", "Studio Raw", 899, "photo-1511556820780-d912e42b4980", "clean-white-tee"),
                        product("Pleated Linen Trousers", "Neutral Studios", 3199, "photo-1473966968600-fa801b869a1a", "pleated-linen-trousers"),
                        product("Minimal White Sneakers", "Common Grounds", 4499, "photo-1595950653106-6c9ebd614d3a", "minimal-white-sneakers")
                ));

        seedOutfit(outfits, minimal, "Everyday Neutrals",
                "A capsule of neutral essentials: tucked tee, straight trousers, and a compact tote.",
                img("photo-1490114538077-0a7f8cb49891"),
                List.of("minimal", "neutrals", "capsule"),
                List.of(
                        product("Box-Fit T-Shirt", "Studio Raw", 799, "photo-1511556820780-d912e42b4980", "box-fit-t-shirt"),
                        product("Straight Trousers", "Neutral Studios", 2599, "photo-1473966968600-fa801b869a1a", "straight-trousers"),
                        product("Compact Leather Tote", "Acro Objects", 3899, "photo-1553062407-98eeb64c6a62", "compact-leather-tote")
                ));

        seedOutfit(outfits, college, "Campus Preppy Core",
                "Preppy campus staples: varsity layer, oxford shirt, and chino shorts for a crisp class-day look.",
                img("photo-1523381210434-271e8be1f52b"),
                List.of("college", "preppy", "campus"),
                List.of(
                        product("Varsity Jacket", "Campus Classics", 4999, "photo-1543076447-215ad9ba6923", "varsity-jacket"),
                        product("Oxford Button-Down", "Heritage Club", 2399, "photo-1521577352947-9bb58764b69a", "oxford-button-down"),
                        product("Pleated Chino Shorts", "Campus Classics", 1699, "photo-1473966968600-fa801b869a1a", "pleated-chino-shorts")
                ));

        seedOutfit(outfits, college, "Library Layering",
                "Long study-session layers: cozy cardigan over a gingham shirt with relaxed trousers.",
                img("photo-1543087903-1ac2ec7aa8c5"),
                List.of("college", "layering", "casual"),
                List.of(
                        product("Wool Knit Cardigan", "Heritage Club", 3499, "photo-1620799140408-edc6dcb6d633", "wool-knit-cardigan"),
                        product("Gingham Shirt", "Campus Classics", 1799, "photo-1521577352947-9bb58764b69a", "gingham-shirt"),
                        product("Relaxed Wide Trousers", "Neutral Studios", 2499, "photo-1473966968600-fa801b869a1a", "relaxed-wide-trousers")
                ));

        seedOutfit(outfits, vintage, "Vintage Film Grain",
                "Faded, lived-in denim and a worn-in tee inspired by golden-hour film photography.",
                img("photo-1506629082955-511b1aa562c8"),
                List.of("vintage", "denim", "retro"),
                List.of(
                        product("Faded Vintage Tee", "Archive Threads", 1299, "photo-1511556820780-d912e42b4980", "faded-vintage-tee"),
                        product("Reworked Wide Jeans", "Archive Threads", 2999, "photo-1541099649105-f69ad21f3246", "reworked-wide-jeans"),
                        product("Suede Cap Toe Boots", "Crown & Co", 4999, "photo-1560343090-f0409e92791a", "suede-cap-toe-boots")
                ));

        seedOutfit(outfits, vintage, "Retro Denim Revival",
                "A revival of 90s denim: indigo wash, boxy denim jacket, and chunky soles.",
                img("photo-1542272604-787c3835535d"),
                List.of("vintage", "denim", "retro"),
                List.of(
                        product("Boxy Denim Jacket", "Mono Denims", 3499, "photo-1541099649105-f69ad21f3246", "boxy-denim-jacket"),
                        product("Wide Indigo Jeans", "Archive Threads", 2799, "photo-1541099649105-f69ad21f3246", "wide-indigo-jeans"),
                        product("Chunky Retro Sneakers", "Retro Flash", 4199, "photo-1556906781-9a412961c28c", "chunky-retro-sneakers")
                ));

        seedOutfit(outfits, vintage, "Thrifted Archive",
                "Archive finds styled together: corduroy layer, graphic band tee, and pleated trousers.",
                img("photo-1534528741775-53994a69daeb"),
                List.of("vintage", "thrift", "archive"),
                List.of(
                        product("Corduroy Overshirt", "Archive Threads", 2199, "photo-1521577352947-9bb58764b69a", "corduroy-overshirt"),
                        product("Band Graphic Tee", "Retro Flash", 1199, "photo-1571945153237-4929e783af4a", "band-graphic-tee"),
                        product("Pleated Corduroy Trousers", "Archive Threads", 2299, "photo-1473966968600-fa801b869a1a", "pleated-corduroy-trousers")
                ));

        seedOutfit(outfits, casual, "Weekend Casual",
                "Easy weekend layers: soft crewneck, relaxed chinos, and clean white low-tops.",
                img("photo-1502716119720-b23a93e5fe1b"),
                List.of("casual", "weekend", "comfort"),
                List.of(
                        product("Soft Crewneck Sweatshirt", "Neutral Studios", 1899, "photo-1620799140408-edc6dcb6d633", "soft-crewneck-sweatshirt"),
                        product("Relaxed Chinos", "Studio Raw", 2299, "photo-1473966968600-fa801b869a1a", "relaxed-chinos"),
                        product("White Low-Top Sneakers", "Common Grounds", 3499, "photo-1595950653106-6c9ebd614d3a", "white-low-top-sneakers")
                ));

        seedOutfit(outfits, casual, "Cafe Corner Casual",
                "A cafe-ready look: knit polo, straight denim, and a crossbody for the essentials.",
                img("photo-1524504388940-b1c1722653e1"),
                List.of("casual", "cafe", "denim"),
                List.of(
                        product("Knit Polo Shirt", "Studio Raw", 1999, "photo-1536700503339-1e4b06520771", "knit-polo-shirt"),
                        product("Straight Denim Jeans", "Mono Denims", 2699, "photo-1541099649105-f69ad21f3246", "straight-denim-jeans"),
                        product("Compact Crossbody Bag", "Acro Objects", 1599, "photo-1553062407-98eeb64c6a62", "compact-crossbody-bag")
                ));

        seedOutfit(outfits, casual, "Errand Day Comfort",
                "Soft fleece, joggers, and easy sneakers built for a full day of errands.",
                img("photo-1503342394128-c104d54dba01"),
                List.of("casual", "comfort", "joggers"),
                List.of(
                        product("Fleece Pullover", "Neutral Studios", 2199, "photo-1556905055-8f358a7a47b2", "fleece-pullover"),
                        product("Ribbed Joggers", "Studio Raw", 1799, "photo-1517445312882-bc9910d016b7", "ribbed-joggers"),
                        product("Everyday Cushioned Sneakers", "Common Grounds", 2999, "photo-1556906781-9a412961c28c", "cushioned-sneakers")
                ));

        seedOutfit(outfits, formal, "Boardroom Tailoring",
                "A sharp two-piece in a deep navy with a crisp white shirt and polished oxfords.",
                img("photo-1500648767791-00dcc994a43e"),
                List.of("formal", "tailoring", "boardroom"),
                List.of(
                        product("Navy Tailored Suit Jacket", "Savile Tailors", 13999, "photo-1507679799987-c73779587ccf", "navy-suit-jacket"),
                        product("Navy Tapered Suit Trousers", "Savile Tailors", 5999, "photo-1473966968600-fa801b869a1a", "navy-suit-trousers"),
                        product("Crisp White Shirt", "Crown & Co", 2299, "photo-1521577352947-9bb58764b69a", "crisp-white-shirt"),
                        product("Polished Oxford Shoes", "Crown & Co", 6999, "photo-1533867617858-e7b97e060509", "polished-oxford-shoes")
                ));

        seedOutfit(outfits, formal, "Evening Gala Fit",
                "Full-gala polish: satin lapel blazer, slim black trousers, and a statement watch.",
                img("photo-1533998839656-76f5e4a2bcc1"),
                List.of("formal", "evening", "luxury"),
                List.of(
                        product("Satin-Lapel Blazer", "Savile Tailors", 15999, "photo-1507679799987-c73779587ccf", "satin-lapel-blazer"),
                        product("Slim Black Trousers", "Savile Tailors", 5499, "photo-1473966968600-fa801b869a1a", "slim-black-trousers"),
                        product("Milanese Mesh Watch", "Crown & Co", 8999, "photo-1523170335258-f5ed11844a49", "milanese-mesh-watch")
                ));

        seedOutfit(outfits, formal, "Sharp Suit Casual",
                "Suited but relaxed: a soft-shoulder blazer over a t-shirt, cut with smooth dark denim.",
                img("photo-1515886657613-9f3515b0c78f"),
                List.of("formal", "smartcasual", "blazer"),
                List.of(
                        product("Soft-Shoulder Blazer", "Savile Tailors", 9999, "photo-1507679799987-c73779587ccf", "soft-shoulder-blazer"),
                        product("Plain Crew Tee", "Studio Raw", 799, "photo-1511556820780-d912e42b4980", "plain-crew-tee"),
                        product("Dark Slim Denim", "Mono Denims", 2799, "photo-1541099649105-f69ad21f3246", "dark-slim-denim"),
                        product("Leather Brogues", "Crown & Co", 6499, "photo-1533867617858-e7b97e060509", "leather-brogues")
                ));

        seedOutfit(outfits, summer, "Summer Whites",
                "White linen and airy layers made for slow summer afternoons.",
                img("photo-1490481651871-ab68de25d43d"),
                List.of("summer", "white", "linen"),
                List.of(
                        product("Linen Camp Shirt", "Neutral Studios", 2199, "photo-1521577352947-9bb58764b69a", "linen-camp-shirt"),
                        product("White Relaxed Trousers", "Studio Raw", 2499, "photo-1473966968600-fa801b869a1a", "white-relaxed-trousers"),
                        product("Leather Sandals", "Common Grounds", 1999, "photo-1512374382149-233c42b6a83b", "leather-sandals")
                ));

        seedOutfit(outfits, summer, "Holiday Linen",
                "Vacation-ready linen co-ord with a woven tote and slide sandals.",
                img("photo-1483985988355-763728e1935b"),
                List.of("summer", "vacation", "linen"),
                List.of(
                        product("Linen Shirt Jacket", "Seoul Studio", 2799, "photo-1521577352947-9bb58764b69a", "linen-shirt-jacket"),
                        product("Linen Co-ord Trousers", "Seoul Studio", 2199, "photo-1473966968600-fa801b869a1a", "linen-co-ord-trousers"),
                        product("Woven Straw Tote", "Acro Objects", 1699, "photo-1553062407-98eeb64c6a62", "woven-straw-tote"),
                        product("Slide Sandals", "Common Grounds", 1499, "photo-1512374382149-233c42b6a83b", "slide-sandals")
                ));

        seedOutfit(outfits, summer, "Beachwalk Light Layers",
                "Tank, airy denim, and bucket sunhat for a long beach walk.",
                img("photo-1509233725247-49e657c54213"),
                List.of("summer", "beach", "light"),
                List.of(
                        product("Ribbed Tank Top", "Studio Raw", 699, "photo-1511556820780-d912e42b4980", "ribbed-tank-top"),
                        product("Airy Wide Denim", "Mono Denims", 2599, "photo-1541099649105-f69ad21f3246", "airy-wide-denim"),
                        product("Bucket Sun Hat", "Acro Objects", 899, "photo-1525507119028-ed4c629a60a3", "bucket-sun-hat")
                ));

        seedOutfit(outfits, winter, "Heavy Winter Layering",
                "Serious cold-weather layering: puffer, knit, and insulated cargos with lug-sole boots.",
                img("photo-1485738422979-f5c462d49f74"),
                List.of("winter", "layering", "cold"),
                List.of(
                        product("Quilted Puffer Coat", "Afterdark", 8999, "photo-1591047139829-d91aecb6caea", "quilted-puffer-coat"),
                        product("Chunky Wool Sweater", "Heritage Club", 5499, "photo-1620799140408-edc6dcb6d633", "chunky-wool-sweater"),
                        product("Insulated Cargo Trousers", "Metro Supply", 3299, "photo-1517445312882-bc9910d016b7", "insulated-cargo-trousers"),
                        product("Lug-Sole Winter Boots", "Crown & Co", 5999, "photo-1560343090-f0409e92791a", "lug-sole-winter-boots")
                ));

        seedOutfit(outfits, winter, "Knitwear Cocoon",
                "Wrapped in soft knits from turtleneck to cuffed trousers for a cocooning winter day.",
                img("photo-1521510895919-46920266ddb3"),
                List.of("winter", "knitwear", "cozy"),
                List.of(
                        product("Ribbed Turtleneck", "Heritage Club", 2999, "photo-1539002847431-2e54497d5ced", "ribbed-turtleneck"),
                        product("Oversized Knit Cardigan", "Neutral Studios", 4499, "photo-1620799140408-edc6dcb6d633", "oversized-knit-cardigan"),
                        product("Cuffed Knit Trousers", "Neutral Studios", 2999, "photo-1473966968600-fa801b869a1a", "cuffed-knit-trousers"),
                        product("Knit Beanie", "Acro Objects", 999, "photo-1525507119028-ed4c629a60a3", "knit-beanie")
                ));

        seedOutfit(outfits, winter, "Street Puffer Night",
                "A cropped puffer stacked over a hoodie and baggy cargos for a street-ready winter.",
                img("photo-1503341504253-dff4815485f1"),
                List.of("winter", "streetwear", "puffer"),
                List.of(
                        product("Cropped Puffer Jacket", "Afterdark", 7499, "photo-1591047139829-d91aecb6caea", "cropped-puffer-jacket"),
                        product("Baggy Cargo Pants", "Metro Supply", 2799, "photo-1517445312882-bc9910d016b7", "baggy-cargo-pants"),
                        product("Padded Hoodie", "Basement Label", 3499, "photo-1556905055-8f358a7a47b2", "padded-hoodie")
                ));

        seedOutfit(outfits, monochrome, "All-Black Edition",
                "Jet-black everything — t-shirt, cargos, and high-tops — for a uniform with attitude.",
                img("photo-1512418490979-92798cec1380"),
                List.of("monochrome", "black", "streetwear"),
                List.of(
                        product("Black Box-Fit Tee", "Basement Label", 899, "photo-1511556820780-d912e42b4980", "black-box-fit-tee"),
                        product("Black Cargo Pants", "Metro Supply", 2399, "photo-1517445312882-bc9910d016b7", "black-cargo-pants"),
                        product("Black High-Top Sneakers", "Common Grounds", 4299, "photo-1595950653106-6c9ebd614d3a", "black-high-top-sneakers")
                ));

        seedOutfit(outfits, monochrome, "Tonal Grey Layer",
                "A single-grey palette from washed tee to charcoal overshirt.",
                img("photo-1495121553079-4c61a1bd76f5"),
                List.of("monochrome", "grey", "layered"),
                List.of(
                        product("Grey Washed Tee", "Neutral Studios", 999, "photo-1511556820780-d912e42b4980", "grey-washed-tee"),
                        product("Charcoal Overshirt", "Studio Raw", 3199, "photo-1521577352947-9bb58764b69a", "charcoal-overshirt"),
                        product("Heathered Joggers", "Neutral Studios", 2099, "photo-1517445312882-bc9910d016b7", "heathered-joggers")
                ));

        seedOutfit(outfits, denim, "Denim-on-Denim",
                "The full double-denim: trucker jacket and matching mid-wash indigo jeans.",
                img("photo-1541099649105-f69ad21f3246"),
                List.of("denim", "double-denim", "casual"),
                List.of(
                        product("Trucker Denim Jacket", "Mono Denims", 3699, "photo-1541099649105-f69ad21f3246", "trucker-denim-jacket"),
                        product("Mid-Wash Straight Jeans", "Mono Denims", 2699, "photo-1541099649105-f69ad21f3246", "mid-wash-straight-jeans"),
                        product("White Low-Court Sneakers", "Common Grounds", 3499, "photo-1595950653106-6c9ebd614d3a", "white-low-court-sneakers")
                ));

        seedOutfit(outfits, denim, "Raw Denim Restyle",
                "Raw indigo selvedge paired with a subtle tee for a clean, craft-heavy finish.",
                img("photo-1475178626620-a4d074967452"),
                List.of("denim", "selvedge", "minimal"),
                List.of(
                        product("Raw Selvedge Jeans", "Mono Denims", 3999, "photo-1541099649105-f69ad21f3246", "raw-selvedge-jeans"),
                        product("Heavyweight Crew Tee", "Studio Raw", 1099, "photo-1511556820780-d912e42b4980", "heavyweight-crew-tee"),
                        product("Brown Leather Belt", "Crown & Co", 1399, "photo-1548036328-c9fa89d128fa", "brown-leather-belt")
                ));

        seedOutfit(outfits, oversized, "Boxy Oversized Silhouette",
                "Maximum ease: a drop-shoulder sweatshirt, wide trousers, and soft-soled sneakers.",
                img("photo-1544441893-675973e31985"),
                List.of("oversized", "boxy", "comfort"),
                List.of(
                        product("Drop-Shoulder Sweatshirt", "Basement Label", 2499, "photo-1620799140408-edc6dcb6d633", "drop-shoulder-sweatshirt"),
                        product("Wide-Band Trousers", "Studio Raw", 2699, "photo-1473966968600-fa801b869a1a", "wide-band-trousers"),
                        product("Soft-Sole Sneakers", "Common Grounds", 3299, "photo-1595950653106-6c9ebd614d3a", "soft-sole-sneakers")
                ));

        seedOutfit(outfits, oversized, "Drapey Oversized Coat",
                "A long drapey overcoat worn loose over a sheer base with straight-leg trousers.",
                img("photo-1521577352947-9bb58764b69a"),
                List.of("oversized", "coat", "layered"),
                List.of(
                        product("Drapey Long Overcoat", "Savile Tailors", 11999, "photo-1509762773665-f07e0bde5d0f", "drapey-long-overcoat"),
                        product("Loose Straight Trousers", "Neutral Studios", 2999, "photo-1473966968600-fa801b869a1a", "loose-straight-trousers"),
                        product("Sneaker-Sole Boots", "Common Grounds", 4899, "photo-1560343090-f0409e92791a", "sneaker-sole-boots")
                ));

        seedOutfit(outfits, oversized, "Oversized Street Layers",
                "Layering volume on volume: a big over-shirt over a hoodie with extra-wide denim.",
                img("photo-1507679799987-c73779587ccf"),
                List.of("oversized", "streetwear", "layers"),
                List.of(
                        product("Extra-Wide Denim", "Metro Supply", 2999, "photo-1541099649105-f69ad21f3246", "extra-wide-denim"),
                        product("Slouchy Hoodie", "Basement Label", 2299, "photo-1556905055-8f358a7a47b2", "slouchy-hoodie"),
                        product("Huge Over-Shirt", "Neutral Studios", 3299, "photo-1521577352947-9bb58764b69a", "huge-over-shirt")
                ));

        outfitRepository.saveAll(outfits);
    }

    private Category ensureCategory(String name, String slug, String description) {
        return categoryRepository.findBySlug(slug)
                .orElseGet(() -> categoryRepository.save(new Category(name, slug, description)));
    }

    private ProductSeed product(String name, String brand, int priceInr, String imageId, String slug) {
        return new ProductSeed(name, brand, priceInr, imageId, slug);
    }

    private void seedOutfit(List<Outfit> outfits, Category category, String title, String description, String imageUrl,
                            List<String> tags, List<ProductSeed> productSeeds) {
        Outfit outfit = outfitRepository.findByTitle(title)
                .orElseGet(() -> {
                    Outfit created = new Outfit(category, title, description, imageUrl);
                    tags.forEach(created::addTag);
                    outfits.add(created);
                    return created;
                });
        syncProducts(outfit, category, productSeeds);
    }

    private void syncProducts(Outfit outfit, Category category, List<ProductSeed> productSeeds) {
        Set<String> seedNames = productSeeds.stream().map(ProductSeed::name).collect(Collectors.toSet());
        outfit.getProducts().removeIf(existing -> !seedNames.contains(existing.getName()));
        Map<String, Product> productsByName = outfit.getProducts().stream()
                .collect(Collectors.toMap(Product::getName, product -> product));
        for (ProductSeed seed : productSeeds) {
            Product existing = productsByName.get(seed.name());
            if (existing == null) {
                Product product = new Product(outfit, seed.name(), seed.brand(), new BigDecimal(seed.priceInr()));
                product.setImageUrl(IMAGE_BASE + seed.imageId() + "?auto=format&fit=crop&w=600&q=80");
                product.setProductUrl("https://example.com/products/" + seed.slug());
                product.setCategory(category);
                outfit.addProduct(product);
            } else {
                existing.setBrand(seed.brand());
                existing.setPrice(new BigDecimal(seed.priceInr()));
                existing.setImageUrl(IMAGE_BASE + seed.imageId() + "?auto=format&fit=crop&w=600&q=80");
                existing.setProductUrl("https://example.com/products/" + seed.slug());
                existing.setCategory(category);
            }
        }
    }

    private String img(String photoId) {
        return IMAGE_BASE + photoId + "?auto=format&fit=crop&w=800&q=80";
    }
}