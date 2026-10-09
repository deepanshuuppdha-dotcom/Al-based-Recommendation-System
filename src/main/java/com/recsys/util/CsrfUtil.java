package com.recsys.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/** Creates and checks per-session CSRF tokens for POST forms. */
public final class CsrfUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    private CsrfUtil() { }

    /** Returns the session token, creating it on first use. */
    public static String token(HttpSession session) {
        String t = (String) session.getAttribute("csrf");
        if (t == null) {
            byte[] b = new byte[24];
            RANDOM.nextBytes(b);
            t = Base64.getUrlEncoder().withoutPadding().encodeToString(b);
            session.setAttribute("csrf", t);
        }
        return t;
    }

    /** True when the submitted "csrf" parameter matches the session token. */
    public static boolean valid(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) return false;
        String expected = (String) s.getAttribute("csrf");
        String got = req.getParameter("csrf");
        return expected != null && got != null
                && MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8),
                                         got.getBytes(StandardCharsets.UTF_8));
    }
}
