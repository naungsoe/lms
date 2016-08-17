package com.hsystems.lms.web;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
public abstract class BaseServlet extends HttpServlet {

  private static final long serialVersionUID = 7424969552657663635L;

  private HttpServletRequest request;

  private HttpServletResponse response;

  protected HttpServletRequest getRequest() {
    return request;
  }

  protected HttpServletResponse getResponse() {
    return response;
  }

  protected void initRequest(
    HttpServletRequest request, HttpServletResponse response) {

    this.request = request;
    this.response = response;    
  }

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    initRequest(request, response);
    doGet();
  }

  @Override
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    initRequest(request, response);
    doPost();
  }

  protected void forwardRequest(String url) 
      throws ServletException, IOException {

    RequestDispatcher dispatcher = request.getRequestDispatcher(url);
    dispatcher.forward(request, response);
  }

  protected void sendRedirect(String url)
      throws IOException {

    response.sendRedirect(url);
  }

  protected abstract void doGet()
      throws ServletException, IOException;

  protected abstract void doPost()
      throws ServletException, IOException;
}
