package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 11/8/16.
 */
@Singleton
public final class AuthenticationFilter extends BaseFilter {

  @Inject
  private AuthenticationService service;

  public void init() throws ServletException {
    getContext().log("AuthenticationFilter initialized");
  }

  public void doFilter()
      throws IOException, ServletException {

    if (isPublicResourceRequest() || service.isAuthenticated(getRequest())) {
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
}
