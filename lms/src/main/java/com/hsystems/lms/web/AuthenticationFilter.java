package com.hsystems.lms.web;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

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
public final class AuthenticationFilter extends BaseFilter {

  public void init() throws ServletException {
    getContext().log("AuthenticationFilter initialized");
  }

  public void doFilter()
      throws IOException, ServletException {

    if (isAuthenticatedAccess()) {
      getFilterChain().doFilter(getRequest(), getResponse());
    } else {
      getContext().log("unauthenticated access request url: "
          + getRequest().getRequestURI());
      forwardRequest("/login");
    }
  }

  protected boolean isAuthenticatedAccess() {
    HttpSession session = getRequest().getSession(false);
    String uri = getRequest().getRequestURI();

    if (uri.startsWith("/login") || uri.startsWith("/static")) {
      return true;
    }

    if (session == null) {
      String id = ServletUtils.getCookieValue(getRequest(), "id");
      return !StringUtils.isEmpty(id);
    } else {
      Object id = session.getAttribute("id");
      return (id != null);
    }
  }
}
