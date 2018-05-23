package com.hsystems.lms.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by naungsoe on 11/8/16.
 */
public abstract class AbstractFilter implements Filter {

  private static final String ACCESS_TOKEN_HEADER = "X-ACCESS-TOKEN";
  private static final String CSRF_TOKEN_HEADER = "X-CSRF-TOKEN";

  protected void forwardRequest(
      ServletRequest request, ServletResponse response, String url)
      throws ServletException, IOException {

    RequestDispatcher dispatcher = request.getRequestDispatcher(url);
    dispatcher.forward(request, response);
  }
}