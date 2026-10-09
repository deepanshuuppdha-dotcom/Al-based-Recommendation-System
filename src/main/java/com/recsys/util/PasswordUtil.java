package com.recsys.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility to generate and verify BCrypt password hashes.
 * Run from the command line:
 *   java -cp <classpath> com.recsys.util.PasswordUtil <plainPassword>
 * The program prints the hash to STDOUT.
 */
public class PasswordUtil {
    /**
     * Generate a BCrypt hash using the default 12‑round salt.
     */
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * Verify a plain password against a BCrypt hash.
     */
    public static boolean verify(String plainPassword, String hash) {
        return BCrypt.checkpw(plainPassword, hash);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: PasswordUtil <plainPassword>");
            System.exit(1);
        }
        String hash = hash(args[0]);
        System.out.println(hash);
    }
}
