package tools;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Generates BCrypt hashes for the given passwords and immediately verifies them.
 * Prints each hash followed by the result of BCrypt.checkpw (true/false).
 */
public class HashGen {
    public static void main(String[] args) {
        String adminPass = "Admin@123";
        String userPass = "User@123";

        // Admin hash (single)
        String adminHash = BCrypt.hashpw(adminPass, BCrypt.gensalt(12));
        System.out.println(adminHash);
        System.out.println(BCrypt.checkpw(adminPass, adminHash));

        // User hashes (five distinct salts)
        for (int i = 0; i < 5; i++) {
            String userHash = BCrypt.hashpw(userPass, BCrypt.gensalt(12));
            System.out.println(userHash);
            System.out.println(BCrypt.checkpw(userPass, userHash));
        }
    }
}
