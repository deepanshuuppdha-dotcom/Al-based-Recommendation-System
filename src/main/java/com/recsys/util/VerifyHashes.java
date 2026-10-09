package com.recsys.util;

public class VerifyHashes {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: <hash> <plainPassword>");
            System.exit(1);
        }
        String hash = args[0];
        String plain = args[1];
        boolean ok = PasswordUtil.verify(plain, hash);
        System.out.println(ok);
    }
}
