package tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Generates a complete db/seed.sql file with INSERT statements for all required tables.
 * Uses the BCrypt hashes produced by HashGen.
 */
public class SeedGen {
    private static final String ADMIN_HASH = "$2a$12$D2W4SHtCkOC6BlChe0V6Y.ShH01fbgKuJpm3HhwCWttVD14voAKsq";
    private static final String[] USER_HASHES = {
        "$2a$12$GbCA9iGer.fAnxcvj8V1j.JHuKi19BgosiTamiZVEVjINngVZZiD6",
        "$2a$12$fwdBwy5m5ryVaa8hhiH4V.8NwVXw6Ie91098Nt57tAtJOMQmUMRJm",
        "$2a$12$LJA6RJJ4eCN4FYvnfVmAMeZO0OgMfSAKGO7wVB9NyH4pn2jP60PIa",
        "$2a$12$re.EJXMCZ2LJERhTmTaXT.49UN8EuKMs5Mb.crCy6ubwzjDDV1vmC",
        "$2a$12$BTxBrPle9NHzvqhiOxXZB.9EENYZ3onYr7zSGu8HEtEZVas5TXsA."
    };
    private static final String[] USER_EMAILS = {
        "alice@example.com",
        "bob@example.com",
        "carol@example.com",
        "dave@example.com",
        "eve@example.com"
    };
    private static final String[] USER_NAMES = {
        "Alice",
        "Bob",
        "Carol",
        "Dave",
        "Eve"
    };
    private static final String[] CATEGORY_NAMES = {
        "Electronics","Books","Clothing","Home & Kitchen",
        "Sports","Beauty","Toys","Automotive"
    };
    private static final String[] INTERACTION_TYPES = {"VIEW","LIKE","CLICK","PURCHASE","DISLIKE"};
    private static final String[] ALGORITHMS = {"HYBRID","CONTENT","COLLAB","POPULARITY"};
    private static final Random RAND = new Random(12345);

    public static void main(String[] args) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter("db/seed.sql"))) {
            w.write("USE recsys_db;\n\n");
            writeUsers(w);
            writeCategories(w);
            writeProducts(w);
            writePreferences(w);
            writeInteractions(w);
            writeSystemSettings(w);
            writeRecommendations(w);
            writeFeedback(w);
        }
    }

    private static void writeUsers(BufferedWriter w) throws IOException {
        w.write("INSERT INTO users (name,email,password_hash,role,is_active) VALUES\n");
        // admin id 1
        w.write("('Admin User','admin@recsys.com','" + ADMIN_HASH + "','ADMIN',TRUE),\n");
        // normal users id 2-6
        for (int i = 0; i < USER_NAMES.length; i++) {
            w.write("('" + USER_NAMES[i] + "','" + USER_EMAILS[i] + "','" + USER_HASHES[i] + "','USER',TRUE)");
            w.write(i == USER_NAMES.length - 1 ? ";\n\n" : ",\n");
        }
    }

    private static void writeCategories(BufferedWriter w) throws IOException {
        w.write("INSERT INTO categories (name) VALUES\n");
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
            w.write("('" + CATEGORY_NAMES[i] + "')");
            w.write(i == CATEGORY_NAMES.length - 1 ? ";\n\n" : ",\n");
        }
    }

    private static void writeProducts(BufferedWriter w) throws IOException {
        w.write("INSERT INTO products (title,description,category_id,tags,price,image_url,popularity_score) VALUES\n");
        int total = 40;
        for (int i = 1; i <= total; i++) {
            int catId = ((i - 1) % CATEGORY_NAMES.length) + 1;
            w.write(String.format("('Product %d','Description %d',%d,'tag%d',%d.99,'https://example.com/img/%d.jpg',%d.0)",
                    i, i, catId, i, i, i, i * 2));
            w.write(i == total ? ";\n\n" : ",\n");
        }
    }

    private static void writePreferences(BufferedWriter w) throws IOException {
        w.write("INSERT INTO user_preferences (user_id,category_id,weight) VALUES\n");
        int line = 0;
        for (int userId = 1; userId <= 6; userId++) {
            for (int catId = 1; catId <= CATEGORY_NAMES.length; catId++) {
                int weight = ((userId + catId) % 5) + 1; // 1..5
                w.write(String.format("(%d,%d,%d)", userId, catId, weight));
                line++;
                if (userId == 6 && catId == CATEGORY_NAMES.length) {
                    w.write(";\n\n");
                } else {
                    w.write(",\n");
                }
            }
        }
    }

    private static void writeInteractions(BufferedWriter w) throws IOException {
        w.write("INSERT INTO interactions (user_id,product_id,type,created_at) VALUES\n");
        int total = 200;
        for (int i = 1; i <= total; i++) {
            int userId = RAND.nextInt(6) + 1;
            int productId = RAND.nextInt(40) + 1;
            String type = INTERACTION_TYPES[RAND.nextInt(INTERACTION_TYPES.length)];
            String ts = String.format("2026-%02d-%02d %02d:%02d:00",
                    RAND.nextInt(2) + 9, // month 9-10
                    RAND.nextInt(28) + 1,
                    RAND.nextInt(24),
                    RAND.nextInt(60));
            w.write(String.format("(%d,%d,'%s','%s')", userId, productId, type, ts));
            if (i == total) {
                w.write(";\n\n");
            } else {
                w.write(",\n");
            }
        }
    }

    private static void writeSystemSettings(BufferedWriter w) throws IOException {
        w.write("INSERT INTO system_settings (setting_key,setting_value,updated_by) VALUES\n");
        String[][] rows = {
            {"algorithm","HYBRID","1"},
            {"weight_content","40","1"},
            {"weight_collab","35","1"},
            {"weight_popularity","25","1"},
            {"max_recommendations","12","1"},
            {"min_score_threshold","0.10","1"},
            {"recency_decay_days","30","1"}
        };
        for (int i = 0; i < rows.length; i++) {
            w.write(String.format("('%s','%s',%s)", rows[i][0], rows[i][1], rows[i][2]));
            w.write(i == rows.length - 1 ? ";\n\n" : ",\n");
        }
    }

    private static void writeRecommendations(BufferedWriter w) throws IOException {
        w.write("INSERT INTO recommendations (user_id,product_id,score,algorithm,generated_at) VALUES\n");
        int total = 30;
        for (int i = 1; i <= total; i++) {
            int userId = RAND.nextInt(6) + 1;
            int productId = RAND.nextInt(40) + 1;
            double score = RAND.nextDouble() * 1.0;
            String algo = ALGORITHMS[i % ALGORITHMS.length];
            String ts = String.format("2026-%02d-%02d %02d:%02d:00",
                    RAND.nextInt(2) + 9,
                    RAND.nextInt(28) + 1,
                    RAND.nextInt(24),
                    RAND.nextInt(60));
            w.write(String.format("(%d,%d,%.3f,'%s','%s')", userId, productId, score, algo, ts));
            w.write(i == total ? ";\n\n" : ",\n");
        }
    }

    private static void writeFeedback(BufferedWriter w) throws IOException {
        w.write("INSERT INTO recommendation_feedback (recommendation_id,shown_at,clicked,converted) VALUES\n");
        int total = 30; // same ids as recommendations inserted above (auto‑increment starts at 1)
        // Desired click counts per algorithm
        int hybridClicks = 0, contentClicks = 0, collabClicks = 0, popularityClicks = 0;
        int targetHybrid = 6, targetContent = 5, targetCollab = 4, targetPopularity = 2;
        int convertedCount = 0;
        int targetConverted = 6; // total converted rows across all algorithms
        for (int i = 1; i <= total; i++) {
            String ts = String.format("2026-%02d-%02d %02d:%02d:00",
                    RAND.nextInt(2) + 9,
                    RAND.nextInt(28) + 1,
                    RAND.nextInt(24),
                    RAND.nextInt(60));
            // Determine algorithm for this recommendation (same logic as writeRecommendations)
            String algo = ALGORITHMS[i % ALGORITHMS.length];
            boolean clicked = false;
            switch (algo) {
                case "HYBRID":
                    if (hybridClicks < targetHybrid) { clicked = true; hybridClicks++; }
                    break;
                case "CONTENT":
                    if (contentClicks < targetContent) { clicked = true; contentClicks++; }
                    break;
                case "COLLAB":
                    if (collabClicks < targetCollab) { clicked = true; collabClicks++; }
                    break;
                case "POPULARITY":
                    if (popularityClicks < targetPopularity) { clicked = true; popularityClicks++; }
                    break;
            }
            boolean converted = clicked && (convertedCount < targetConverted);
            if (converted) { convertedCount++; }
            w.write(String.format("(%d,'%s',%s,%s)", i, ts, clicked ? "TRUE" : "FALSE", converted ? "TRUE" : "FALSE"));
            w.write(i == total ? ";\n" : ",\n");
        }
    }
}
