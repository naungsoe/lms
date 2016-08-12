package com.hsystems.lms.web;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 11/8/16.
 */
@Singleton
public final class AuthenticationFilter implements Filter {

  private ServletContext context;

  public void init(FilterConfig filterConfig)
      throws ServletException {

    this.context = filterConfig.getServletContext();
    this.context.log("AuthenticationFilter initialized");
  }

  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest)servletRequest;
    HttpServletResponse response = (HttpServletResponse)servletResponse;

    if (isAuthenticatedAccess(request)) {
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      this.context.log("Unauthorized access request");
      request.getRequestDispatcher("/login")
          .forward(request, response);
    }
  }

  private boolean isAuthenticatedAccess(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    String uri = request.getRequestURI();
    return (uri.startsWith("/login") || (session != null));
  }

  public void destroy() {

  }
}
