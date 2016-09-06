package com.hsystems.lms.web;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 11/8/16.
 */
public abstract class BaseFilter implements Filter {

  private ServletContext context;

  private HttpServletRequest request;

  private HttpServletResponse response;

  private FilterChain filterChain;

  protected ServletContext getContext() {
    return context;
  }

  protected HttpServletRequest getRequest() {
    return request;
  }

  protected HttpServletResponse getResponse() {
    return response;
  }

  protected FilterChain getFilterChain() {
    return filterChain;
  }

  public void init(FilterConfig filterConfig)
      throws ServletException {

    this.context = filterConfig.getServletContext();
    init();
  }

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {

    this.request = (HttpServletRequest) request;
    this.response = (HttpServletResponse) response;
    this.filterChain = filterChain;
    doFilter();
  }

  public void destroy() {

  }

  public void forwardRequest(String url)
      throws ServletException, IOException {

    RequestDispatcher dispatcher = request.getRequestDispatcher(url);
    dispatcher.forward(request, response);
  }

  public abstract void init() throws ServletException;

  public abstract void doFilter() throws IOException, ServletException;
}
