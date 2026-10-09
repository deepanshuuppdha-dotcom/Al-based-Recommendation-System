package com.recsys.servlet;

import com.recsys.dao.impl.UserPreferenceDAOImpl;
import com.recsys.model.User;
import com.recsys.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** User dashboard (recommendations are added in a later phase). */
@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        req.setAttribute("prefCount", new UserPreferenceDAOImpl().findByUser(u.getId()).size());
        req.setAttribute("csrf", CsrfUtil.token(req.getSession()));
        req.getRequestDispatcher("/WEB-INF/views/user/dashboard.jsp").forward(req, res);
    }
}
