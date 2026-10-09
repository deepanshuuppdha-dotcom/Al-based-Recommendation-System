package com.recsys.servlet;

import com.recsys.util.CsrfUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Ends the session (POST only, CSRF protected). */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!CsrfUtil.valid(req)) { res.sendError(403, "Invalid CSRF token"); return; }
        req.getSession().invalidate();
        res.sendRedirect(req.getContextPath() + "/login");
    }
}
