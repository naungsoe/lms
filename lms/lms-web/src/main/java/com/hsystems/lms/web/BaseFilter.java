package com.hsystems.lms.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by naungsoe on 11/8/16.
 */
public abstract class BaseFilter implements Filter {

  protected String getRequestUrl(ServletRequest request) {
    return ((HttpServletRequest) request).getRequestURI();
  }

  protected void forwardRequest(
      ServletRequest request, ServletResponse response, String url)
      throws ServletException, IOException {

    RequestDispatcher dispatcher = request.getRequestDispatcher(url);
    dispatcher.forward(request, response);
  }
}
