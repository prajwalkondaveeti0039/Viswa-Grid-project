package com.viswagrid.controller;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {
        "/admin-dashboard",
        "/admin-dashboard.jsp",
        "/admin/*"
})
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        // Prevent browser from caching admin pages
        httpResponse.setHeader(
                "Cache-Control",
                "no-cache, no-store, must-revalidate"
        );

        httpResponse.setHeader("Pragma", "no-cache");

        httpResponse.setDateHeader("Expires", 0);

        // Check existing administrator session
        HttpSession session =
                httpRequest.getSession(false);

        boolean loggedIn = session != null
                && session.getAttribute("admin") != null;

        if (loggedIn) {

            // Allow authenticated administrator
            chain.doFilter(request, response);

        } else {

            // Redirect unauthenticated visitors
            httpResponse.sendRedirect(
                    httpRequest.getContextPath()
                    + "/admin-login.jsp"
            );
        }
    }
}