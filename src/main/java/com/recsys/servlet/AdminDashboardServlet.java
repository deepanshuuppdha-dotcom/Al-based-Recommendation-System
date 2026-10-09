package com.recsys.servlet;

import com.recsys.dao.impl.InteractionDAOImpl;
import com.recsys.dao.impl.ProductDAOImpl;
import com.recsys.dao.impl.UserDAOImpl;
import com.recsys.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Admin dashboard with live counts from the database. */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("totalUsers", new UserDAOImpl().findAll().size());
        req.setAttribute("totalProducts", new ProductDAOImpl().findAll().size());
        req.setAttribute("totalInteractions", new InteractionDAOImpl().count());
        req.setAttribute("csrf", CsrfUtil.token(req.getSession()));
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, res);
    }
}
