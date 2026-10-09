package com.recsys.servlet;

import com.recsys.exception.ValidationException;
import com.recsys.service.AuthService;
import com.recsys.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Registration form and account creation (Post/Redirect/Get). */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("csrf", CsrfUtil.token(req.getSession()));
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!CsrfUtil.valid(req)) { res.sendError(403, "Invalid CSRF token"); return; }
        try {
            auth.register(req.getParameter("name"), req.getParameter("email"), req.getParameter("password"));
            res.sendRedirect(req.getContextPath() + "/login?registered=1");
        } catch (ValidationException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("name", req.getParameter("name"));
            req.setAttribute("email", req.getParameter("email"));
            req.setAttribute("csrf", CsrfUtil.token(req.getSession()));
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, res);
        }
    }
}
