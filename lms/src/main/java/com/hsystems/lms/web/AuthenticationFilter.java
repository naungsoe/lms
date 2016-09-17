package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 11/8/16.
 */
public final class AuthenticationFilter extends BaseFilter {

  @Inject
  private AuthenticationService service;

  public void init()
      throws ServletException {

    getContext().log("AuthenticationFilter initialized");
  }

  public void doFilter()
      throws IOException, ServletException {

    if (isPublicResourceRequest() || isAuthenticated(getRequest())) {
      getFilterChain().doFilter(getRequest(), getResponse());
    } else {
      getContext().log("unauthenticated access request url : "
          + getRequest().getRequestURI());
      forwardRequest("/web/signin");
    }
  }

  public boolean isPublicResourceRequest() {
    String url = getRequest().getRequestURI();
    return url.startsWith("/web/signin")
        || url.startsWith("/web/accounthelp")
        || url.startsWith("/web/signup")
        || url.startsWith("/web/error");
  }

  public boolean isAuthenticated(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    return (session != null) && (session.getAttribute("id") != null);
  }
}
