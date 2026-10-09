package com.recsys.servlet;

import com.recsys.exception.AuthenticationException;
import com.recsys.model.Role;
import com.recsys.model.User;
import com.recsys.service.AuthService;
import com.recsys.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Shows the login form and signs users in (session id is rotated on success). */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService auth = new AuthService();

    /** Dashboard URL for a user's role. */
    public static String homeFor(User u, HttpServletRequest req) {
        return req.getContextPath() + (u.getRole() == Role.ADMIN ? "/admin/dashboard" : "/user/dashboard");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession s = req.getSession();
        if (s.getAttribute("user") != null) { res.sendRedirect(homeFor((User) s.getAttribute("user"), req)); return; }
        req.setAttribute("csrf", CsrfUtil.token(s));
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!CsrfUtil.valid(req)) { res.sendError(403, "Invalid CSRF token"); return; }
        try {
            User u = auth.login(req.getParameter("email"), req.getParameter("password"));
            req.changeSessionId();
            req.getSession().setAttribute("user", u);
            res.sendRedirect(homeFor(u, req));
        } catch (AuthenticationException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("email", req.getParameter("email"));
            req.setAttribute("csrf", CsrfUtil.token(req.getSession()));
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
        }
    }
}
