package com.recsys.filter;

import com.recsys.model.Role;
import com.recsys.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Blocks anonymous access and enforces ADMIN / USER areas. */
@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) rq;
        HttpServletResponse res = (HttpServletResponse) rs;
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        boolean open = path.startsWith("/assets") || path.equals("/login")
                || path.equals("/register") || path.equals("/index.jsp") || path.equals("/");
        if (open) { chain.doFilter(rq, rs); return; }

        HttpSession s = req.getSession(false);
        User u = s == null ? null : (User) s.getAttribute("user");
        if (u == null) { res.sendRedirect(req.getContextPath() + "/login"); return; }
        if (path.startsWith("/admin") && u.getRole() != Role.ADMIN) { res.sendError(403); return; }
        if (path.startsWith("/user") && u.getRole() != Role.USER) { res.sendError(403); return; }
        chain.doFilter(rq, rs);
    }
}
