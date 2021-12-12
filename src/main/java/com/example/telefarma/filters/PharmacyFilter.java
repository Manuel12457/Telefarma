package com.example.telefarma.filters;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "PharmacyFilter", servletNames = {"PharmacyServlet"})
public class PharmacyFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String rol = (String) request.getSession().getAttribute("rol");

        if (rol == null) {
            response.sendRedirect(request.getContextPath());
        } else {
            if (rol.equals("client")) {
                response.sendRedirect(request.getContextPath() + "/ClientServlet");
            } else if (rol.equals("pharmacy")) {
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                chain.doFilter(request, response);
            } else if (rol.equals("admin")) {
                response.sendRedirect(request.getContextPath() + "/AdminServlet");
            }
        }

    }
}
