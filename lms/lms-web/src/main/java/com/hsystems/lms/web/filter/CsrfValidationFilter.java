package com.hsystems.lms.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by naungsoe on 11/8/16.
 */
public class CsrfValidationFilter
    extends BaseFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig)
      throws ServletException {

    ServletContext context = filterConfig.getServletContext();
    context.log("CSRFFilter initialized");
  }

  @Override
  public void doFilter(
      ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {

  }
}
