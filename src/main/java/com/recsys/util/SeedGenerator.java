package com.recsys.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Generates a complete db/seed.sql file with deterministic sample data.
 * The file contains INSERT statements for:
 *   - 8 categories
 *   - 40 products (spread across categories)
 *   - 6 users (1 admin + 5 normal) using real BCrypt hashes
 *   - user_preferences for every user/category (random weight 1‑5)
 *   - 200 interactions (random type, spread over last 30 days)
 *   - 30 recommendations with feedback (random scores & dates)
 *   - 7 system_settings rows (default values)
 *
 * Run after compiling the project (classpath must include jbcrypt).
 * Example: java -cp "C:\dev\tmp\classes;C:\dev\tmp\jbcrypt-0.4.jar" com.recsys.util.SeedGenerator
 */
public class SeedGenerator {
    private static final String[] CATEGORIES = {
        "Electronics", "Books", "Clothing", "Home & Kitchen",
        "Sports", "Beauty", "Toys", "Automotive"
    };
    private static final String[] PRODUCT_TITLES = {
        "Wireless Mouse", "Bluetooth Headphones", "Java Programming Book", "Mystery Novel",
        "Men's Casual Shirt", "Women's Summer Dress", "Stainless Steel Cookware Set", "LED Desk Lamp",
        "Yoga Mat", "Running Shoes", "Facial Cleanser", "Lipstick Set", "Building Blocks Set", "Remote Control Car",
        "Car Vacuum Cleaner", "Smartphone Case", "Wireless Charger", "Noise Cancelling Earbuds",
        "Gaming Keyboard", "E-Reader", "Winter Jacket", "Blender", "Fitness Tracker", "Scented Candle",
        "Action Figure", "Car Seat Cover", "Water Bottle", "Sunglasses", "Backpack", "Coffee Maker",
        "LED Monitor", "Desk Organizer", "Bluetooth Speaker", "Cookbook", "Hiking Boots", "Hair Dryer",
        "Puzzle Set", "Dash Cam", "Electric Toothbrush", "Smartwatch"
    };
    private static final String[] PRODUCT_DESCRIPTIONS = {
        "High quality", "Top seller", "Best value", "Customer favorite",
        "Limited edition", "Eco friendly", "Premium design", "Compact", "Durable", "Lightweight"
    };
    private static final String[] INTERACTION_TYPES = {"VIEW","LIKE","CLICK","PURCHASE","DISLIKE"};
    private static final Random RAND = new Random(12345); // deterministic
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Real BCrypt hashes generated earlier
    private static final String ADMIN_HASH = "$2a$12$lEaRJOurBn3l3NIWa6/NW.OJRd.Q16GIpKjCK7kQ6MGdlNiboZ3vS";
    private static final String USER_HASH = "$2a$12$DxisYpNJJln33fav4KlWneMan3Arv90hSrKe5auwa5eIuOZP65N0m";

    public static void main(String[] args) throws IOException {
        try (FileWriter fw = new FileWriter("db/seed.sql")) {
            fw.write("USE recsys_db;\n\n");
            writeUsers(fw);
            writeCategories(fw);
            writeProducts(fw);
            writePreferences(fw);
            writeInteractions(fw);
            writeRecommendations(fw);
            writeSystemSettings(fw);
        }
        System.out.println("seed.sql generated successfully.");
    }

    private static void writeUsers(FileWriter fw) throws IOException {
        fw.write("-- Users\n");
        fw.write("INSERT INTO users (name, email, password_hash, role, is_active) VALUES\n");
        fw.write("('Admin User','admin@recsys.com','" + ADMIN_HASH + "','ADMIN',TRUE),\n");
        String[] names = {"Alice","Bob","Carol","Dave","Eve"};
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String email = name.toLowerCase() + "@example.com";
            fw.write("('" + name + "','" + email + "','" + USER_HASH + "','USER',TRUE)" + (i == names.length - 1 ? ";\n\n" : ",\n"));
        }
    }

    private static void writeCategories(FileWriter fw) throws IOException {
        fw.write("-- Categories\n");
        fw.write("INSERT INTO categories (name) VALUES\n");
        for (int i = 0; i < CATEGORIES.length; i++) {
            fw.write("('" + CATEGORIES[i] + "')" + (i == CATEGORIES.length - 1 ? ";\n\n" : ",\n"));
        }
    }

    private static void writeProducts(FileWriter fw) throws IOException {
        fw.write("-- Products (40 rows)\n");
        fw.write("INSERT INTO products (title, description, category_id, tags, price, image_url, popularity_score) VALUES\n");
        int prodId = 1;
        for (int i = 0; i < PRODUCT_TITLES.length; i++) {
            String title = PRODUCT_TITLES[i];
            String desc = PRODUCT_DESCRIPTIONS[RAND.nextInt(PRODUCT_DESCRIPTIONS.length)];
            int catId = (i % CATEGORIES.length) + 1; // simple distribution
            String tags = title.toLowerCase().replace(' ', ',');
            double price = 10 + RAND.nextInt(200);
            String img = "https://example.com/img/" + prodId + ".jpg";
            double pop = 50 + RAND.nextDouble() * 50;
            fw.write(String.format("('%s','%s',%d,'%s',%.2f,'%s',%.2f)",
                    title, desc, catId, tags, price, img, pop));
            fw.write(i == PRODUCT_TITLES.length - 1 ? ";\n\n" : ",\n");
            prodId++;
        }
    }

    private static void writePreferences(FileWriter fw) throws IOException {
        fw.write("-- User Preferences (each user gets a weight per category)\n");
        fw.write("INSERT INTO user_preferences (user_id, category_id, weight) VALUES\n");
        // user IDs will be 1..6 (admin + 5 users)
        for (int userId = 1; userId <= 6; userId++) {
            for (int catId = 1; catId <= CATEGORIES.length; catId++) {
                int weight = RAND.nextInt(5) + 1; // 1‑5
                fw.write(String.format("(%d,%d,%d)", userId, catId, weight));
                boolean last = (userId == 6 && catId == CATEGORIES.length);
                fw.write(last ? ";\n\n" : ",\n");
            }
        }
    }

    private static void writeInteractions(FileWriter fw) throws IOException {
        fw.write("-- Interactions (200 rows)\n");
        fw.write("INSERT INTO interactions (user_id, product_id, type, created_at) VALUES\n");
        int total = 200;
        for (int i = 0; i < total; i++) {
            int userId = RAND.nextInt(6) + 1;
            int productId = RAND.nextInt(PRODUCT_TITLES.length) + 1;
            String type = INTERACTION_TYPES[RAND.nextInt(INTERACTION_TYPES.length)];
            LocalDateTime ts = LocalDateTime.now().minusDays(RAND.nextInt(30)).withHour(RAND.nextInt(24)).withMinute(RAND.nextInt(60)).withSecond(0);
            fw.write(String.format("(%d,%d,'%s','%s')", userId, productId, type, ts.format(DT)));
            fw.write(i == total - 1 ? ";\n\n" : ",\n");
        }
    }

    private static void writeRecommendations(FileWriter fw) throws IOException {
        fw.write("-- Recommendations (30 rows)\n");
        fw.write("INSERT INTO recommendations (user_id, product_id, score, algorithm, generated_at) VALUES\n");
        int total = 30;
        for (int i = 0; i < total; i++) {
            int userId = RAND.nextInt(6) + 1;
            int productId = RAND.nextInt(PRODUCT_TITLES.length) + 1;
            double score = 0.1 + RAND.nextDouble() * 0.9; // 0.1‑1.0
            String algorithm = new String[]{"CONTENT","COLLAB","POPULARITY","HYBRID"}[RAND.nextInt(4)];
            LocalDateTime gen = LocalDateTime.now().minusDays(RAND.nextInt(30)).withHour(RAND.nextInt(24)).withMinute(RAND.nextInt(60)).withSecond(0);
            fw.write(String.format("(%d,%d,%.3f,'%s','%s')", userId, productId, score, algorithm, gen.format(DT)));
            fw.write(i == total - 1 ? ";\n\n" : ",\n");
        }
        // Feedback rows (one per recommendation, random click/convert)
        fw.write("INSERT INTO recommendation_feedback (recommendation_id, shown_at, clicked, converted) VALUES\n");
        for (int i = 1; i <= total; i++) {
            LocalDateTime shown = LocalDateTime.now().minusDays(RAND.nextInt(30)).withHour(RAND.nextInt(24)).withMinute(RAND.nextInt(60)).withSecond(0);
            boolean clicked = RAND.nextDouble() < 0.3;
            boolean converted = clicked && RAND.nextDouble() < 0.5;
            fw.write(String.format("(%d,'%s',%s,%s)", i, shown.format(DT), clicked ? "TRUE" : "FALSE", converted ? "TRUE" : "FALSE"));
            fw.write(i == total ? ";\n\n" : ",\n");
        }
    }

    private static void writeSystemSettings(FileWriter fw) throws IOException {
        fw.write("-- System Settings (default values)\n");
        fw.write("INSERT INTO system_settings (setting_key, setting_value, updated_by) VALUES\n");
        String[][] rows = {
                {"algorithm","HYBRID"},
                {"weight_content","40"},
                {"weight_collab","35"},
                {"weight_popularity","25"},
                {"max_recommendations","12"},
                {"min_score_threshold","0.10"},
                {"recency_decay_days","30"}
        };
        for (int i = 0; i < rows.length; i++) {
            fw.write(String.format("('%s','%s',1)", rows[i][0], rows[i][1]));
            fw.write(i == rows.length - 1 ? ";\n" : ",\n");
        }
    }
}
